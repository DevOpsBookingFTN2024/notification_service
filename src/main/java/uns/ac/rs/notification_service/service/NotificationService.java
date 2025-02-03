package uns.ac.rs.notification_service.service;

import org.springframework.stereotype.Service;
import uns.ac.rs.notification_service.dto.NotificationDTO;
import uns.ac.rs.notification_service.dto.client.UserDTO;
import uns.ac.rs.notification_service.dto.request.CreateNotificationRequest;
import uns.ac.rs.notification_service.dto.response.MessageResponse;
import uns.ac.rs.notification_service.mapper.NotificationMapper;
import uns.ac.rs.notification_service.model.ENotificationType;
import uns.ac.rs.notification_service.model.GuestNotificationSettings;
import uns.ac.rs.notification_service.model.HostNotificationSettings;
import uns.ac.rs.notification_service.model.Notification;
import uns.ac.rs.notification_service.repository.GuestNotificationSettingsRepository;
import uns.ac.rs.notification_service.repository.HostNotificationSettingsRepository;
import uns.ac.rs.notification_service.repository.NotificationRepository;
import uns.ac.rs.notification_service.service.client.UserServiceClient;
import uns.ac.rs.notification_service.service.socket.NotificationWebSocketService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private final UserServiceClient userServiceClient;

    private final HostNotificationSettingsRepository hostNotificationSettingsRepository;

    private final GuestNotificationSettingsRepository guestNotificationSettingsRepository;

    private final NotificationWebSocketService notificationWebSocketService;

    public NotificationService(NotificationRepository notificationRepository,
                               UserServiceClient userServiceClient,
                               HostNotificationSettingsRepository hostNotificationSettingsRepository,
                               GuestNotificationSettingsRepository guestNotificationSettingsRepository,
                               NotificationWebSocketService notificationWebSocketService) {
        this.notificationRepository = notificationRepository;
        this.userServiceClient = userServiceClient;
        this.hostNotificationSettingsRepository = hostNotificationSettingsRepository;
        this.guestNotificationSettingsRepository = guestNotificationSettingsRepository;
        this.notificationWebSocketService = notificationWebSocketService;
    }

    //metodu koriste ReservationService i RatingService
    public MessageResponse createNotification(CreateNotificationRequest createNotificationRequest,
                                              String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }

        if (userDetails.getRoles().contains("ROLE_HOST")) {
            return createGuestNotification(createNotificationRequest);
        } else if (userDetails.getRoles().contains("ROLE_GUEST")) {
            return createHostNotification(createNotificationRequest);
        } else {
            throw new SecurityException("User do not have permission for this action.");
        }
    }

    public List<NotificationDTO> getAllMyNotifications(String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_GUEST") && !userDetails.getRoles().contains("ROLE_HOST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        return notificationRepository.findByRecipient(userDetails.getUsername())
                .stream()
                .map(NotificationMapper::toNotificationDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getAllMyUnreadNotifications(String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_GUEST") && !userDetails.getRoles().contains("ROLE_HOST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        return notificationRepository.findByRecipient(userDetails.getUsername())
                .stream()
                .filter(notification -> !notification.getIsRead())
                .map(NotificationMapper::toNotificationDTO)
                .collect(Collectors.toList());
    }

    public MessageResponse readNotification(String id, String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_GUEST") && !userDetails.getRoles().contains("ROLE_HOST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notification not found with id: " + id));

        if (!Objects.equals(notification.getRecipient(), userDetails.getUsername())) {
            throw new SecurityException("This is not your notification.");
        }

        notification.setIsRead(true);

        notificationRepository.save(notification);

        return new MessageResponse("Notification read successfully.");
    }

    public MessageResponse deleteNotification(String id, String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_GUEST") && !userDetails.getRoles().contains("ROLE_HOST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notification not found with id: " + id));

        if (!Objects.equals(notification.getRecipient(), userDetails.getUsername())) {
            throw new SecurityException("This is not your notification.");
        }

        notificationRepository.delete(notification);

        return new MessageResponse("Notification deleted successfully.");
    }


    private MessageResponse createHostNotification(CreateNotificationRequest createNotificationRequest) {
        HostNotificationSettings hostNotificationSettings = hostNotificationSettingsRepository
                .findByHost(createNotificationRequest.getRecipient());
        if (hostNotificationSettings == null) {
            return new MessageResponse("Notifications are not enabled.");
        }

        ENotificationType notificationType = ENotificationType.valueOf(createNotificationRequest.getType());

        if (notificationType != ENotificationType.RESERVATION_REQUEST
                && notificationType != ENotificationType.RESERVATION_CANCELED
                && notificationType != ENotificationType.HOST_RATED
                && notificationType != ENotificationType.ACCOMMODATION_RATED) {
            throw new IllegalArgumentException("This type of notification cannot be created for host.");
        }

        if (!isNotificationEnabledForHost(notificationType, hostNotificationSettings)) {
            return new MessageResponse(notificationType + " notifications are disabled.");
        }

        Notification newNotification = new Notification(
                createNotificationRequest.getRecipient(),
                createNotificationRequest.getMessage(),
                ENotificationType.valueOf(createNotificationRequest.getType())
        );

        newNotification.setDateTime(LocalDateTime.now());
        newNotification.setIsRead(false);

        notificationRepository.save(newNotification);

        NotificationDTO newNotificationDTO = NotificationMapper.toNotificationDTO(newNotification);
        notificationWebSocketService.sendNotification(newNotificationDTO);

        return new MessageResponse("Notification created successfully.");
    }

    private boolean isNotificationEnabledForHost(ENotificationType type, HostNotificationSettings settings) {
        return switch (type) {
            case RESERVATION_REQUEST -> Boolean.TRUE.equals(settings.getIsReservationRequestEnabled());
            case RESERVATION_CANCELED -> Boolean.TRUE.equals(settings.getIsReservationCanceledEnabled());
            case HOST_RATED -> Boolean.TRUE.equals(settings.getIsHostRatedEnabled());
            case ACCOMMODATION_RATED -> Boolean.TRUE.equals(settings.getIsAccommodationRatedEnabled());
            default -> false;
        };
    }

    private MessageResponse createGuestNotification(CreateNotificationRequest createNotificationRequest) {
        GuestNotificationSettings guestNotificationSettings = guestNotificationSettingsRepository
                .findByGuest(createNotificationRequest.getRecipient());
        if (guestNotificationSettings == null) {
            return new MessageResponse("Notifications are not enabled.");
        }

        ENotificationType notificationType = ENotificationType.valueOf(createNotificationRequest.getType());

        if (notificationType != ENotificationType.RESERVATION_RESPONSE) {
            throw new IllegalArgumentException("This type of notification cannot be created for guest.");
        }

        if (!guestNotificationSettings.getIsReservationResponseEnabled()) {
            return new MessageResponse(notificationType + " notifications are disabled.");
        }

        Notification newNotification = new Notification(
                createNotificationRequest.getRecipient(),
                createNotificationRequest.getMessage(),
                ENotificationType.valueOf(createNotificationRequest.getType())
        );

        newNotification.setDateTime(LocalDateTime.now());
        newNotification.setIsRead(false);

        notificationRepository.save(newNotification);

        NotificationDTO newNotificationDTO = NotificationMapper.toNotificationDTO(newNotification);
        notificationWebSocketService.sendNotification(newNotificationDTO);

        return new MessageResponse("Notification created successfully.");
    }
}
