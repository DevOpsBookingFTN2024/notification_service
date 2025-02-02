package uns.ac.rs.notification_service.service;

import org.springframework.stereotype.Service;
import uns.ac.rs.notification_service.dto.GuestNotificationSettingsDTO;
import uns.ac.rs.notification_service.dto.client.UserDTO;
import uns.ac.rs.notification_service.dto.request.UpdateGuestNotificationSettingsRequest;
import uns.ac.rs.notification_service.dto.response.MessageResponse;
import uns.ac.rs.notification_service.mapper.GuestNotificationSettingsMapper;
import uns.ac.rs.notification_service.model.GuestNotificationSettings;
import uns.ac.rs.notification_service.repository.GuestNotificationSettingsRepository;
import uns.ac.rs.notification_service.service.client.UserServiceClient;

@Service
public class GuestNotificationSettingsService {
    private final GuestNotificationSettingsRepository guestNotificationSettingsRepository;

    private final UserServiceClient userServiceClient;

    public GuestNotificationSettingsService(GuestNotificationSettingsRepository guestNotificationSettingsRepository,
                                            UserServiceClient userServiceClient) {
        this.guestNotificationSettingsRepository = guestNotificationSettingsRepository;
        this.userServiceClient = userServiceClient;
    }

    //metodu koristi UserService
    public MessageResponse createGuestNotificationSettings(String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_GUEST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        if (!guestNotificationSettingsRepository.existsByGuest(userDetails.getUsername())) {
            GuestNotificationSettings newGuestNotificationSettings = new GuestNotificationSettings(
                    userDetails.getUsername(),
                    true
            );

            guestNotificationSettingsRepository.save(newGuestNotificationSettings);

            return new MessageResponse("Guest notification settings created successfully.");
        } else {
            throw new IllegalArgumentException("Guest notification settings already exist.");
        }
    }

    public GuestNotificationSettingsDTO getMyGuestNotificationSettings(String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_GUEST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        GuestNotificationSettings guestNotificationSettings = guestNotificationSettingsRepository
                .findByGuest(userDetails.getUsername());

        return GuestNotificationSettingsMapper.toGuestNotificationSettingsDTO(guestNotificationSettings);
    }

    public MessageResponse updateMyGuestNotificationSettings(
            UpdateGuestNotificationSettingsRequest updateGuestNotificationSettingsRequest,
            String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_GUEST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        GuestNotificationSettings guestNotificationSettings = guestNotificationSettingsRepository
                .findByGuest(userDetails.getUsername());

        guestNotificationSettings.setIsReservationResponseEnabled(
                updateGuestNotificationSettingsRequest.getIsReservationResponseEnabled());

        guestNotificationSettingsRepository.save(guestNotificationSettings);

        return new MessageResponse("Guest notification settings updated successfully.");
    }
}
