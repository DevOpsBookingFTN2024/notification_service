package uns.ac.rs.notification_service.service;

import org.springframework.stereotype.Service;
import uns.ac.rs.notification_service.dto.HostNotificationSettingsDTO;
import uns.ac.rs.notification_service.dto.client.UserDTO;
import uns.ac.rs.notification_service.dto.request.UpdateHostNotificationSettingsRequest;
import uns.ac.rs.notification_service.dto.response.MessageResponse;
import uns.ac.rs.notification_service.mapper.HostNotificationSettingsMapper;
import uns.ac.rs.notification_service.model.GuestNotificationSettings;
import uns.ac.rs.notification_service.model.HostNotificationSettings;
import uns.ac.rs.notification_service.repository.HostNotificationSettingsRepository;
import uns.ac.rs.notification_service.service.client.UserServiceClient;

@Service
public class HostNotificationSettingsService {
    private final HostNotificationSettingsRepository hostNotificationSettingsRepository;

    private final UserServiceClient userServiceClient;

    public HostNotificationSettingsService(HostNotificationSettingsRepository hostNotificationSettingsRepository,
                                           UserServiceClient userServiceClient) {
        this.hostNotificationSettingsRepository = hostNotificationSettingsRepository;
        this.userServiceClient = userServiceClient;
    }

    //metodu koristi UserService
    public MessageResponse createHostNotificationSettings(String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_HOST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        if (!hostNotificationSettingsRepository.existsByHost(userDetails.getUsername())) {
            HostNotificationSettings newHostNotificationSettings = new HostNotificationSettings(
                    userDetails.getUsername(),
                    true,
                    true,
                    true,
                    true
            );

            hostNotificationSettingsRepository.save(newHostNotificationSettings);

            return new MessageResponse("Host notification settings created successfully.");
        } else {
            throw new IllegalArgumentException("Host notification settings already exist.");
        }
    }

    public HostNotificationSettingsDTO getMyHostNotificationSettings(String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_HOST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        HostNotificationSettings hostNotificationSettings = hostNotificationSettingsRepository
                .findByHost(userDetails.getUsername());

        return HostNotificationSettingsMapper.toHostNotificationSettingsDTO(hostNotificationSettings);
    }

    public MessageResponse updateMyHostNotificationSettings(
            UpdateHostNotificationSettingsRequest updateHostNotificationSettingsRequest,
            String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_HOST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        HostNotificationSettings hostNotificationSettings = hostNotificationSettingsRepository
                .findByHost(userDetails.getUsername());

        hostNotificationSettings.setIsReservationRequestEnabled(
                updateHostNotificationSettingsRequest.getIsReservationRequestEnabled());
        hostNotificationSettings.setIsReservationCanceledEnabled(
                updateHostNotificationSettingsRequest.getIsReservationCanceledEnabled());
        hostNotificationSettings.setIsHostRatedEnabled(
                updateHostNotificationSettingsRequest.getIsHostRatedEnabled());
        hostNotificationSettings.setIsAccommodationRatedEnabled(
                updateHostNotificationSettingsRequest.getIsAccommodationRatedEnabled());

        hostNotificationSettingsRepository.save(hostNotificationSettings);

        return new MessageResponse("Host notification settings updated successfully.");
    }

    public boolean isHostHasEnabledNotifications(String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_HOST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        HostNotificationSettings hostNotificationSettings = hostNotificationSettingsRepository
                .findByHost(userDetails.getUsername());

        return hostNotificationSettings != null;
    }
}
