package uns.ac.rs.notification_service.mapper;

import uns.ac.rs.notification_service.dto.HostNotificationSettingsDTO;
import uns.ac.rs.notification_service.model.HostNotificationSettings;

public class HostNotificationSettingsMapper {
    public static HostNotificationSettingsDTO toHostNotificationSettingsDTO(
                                              HostNotificationSettings hostNotificationSettings) {
        return HostNotificationSettingsDTO.builder()
                .id(hostNotificationSettings.getId())
                .host(hostNotificationSettings.getHost())
                .isReservationRequestEnabled(hostNotificationSettings.getIsReservationRequestEnabled())
                .isReservationCanceledEnabled(hostNotificationSettings.getIsReservationCanceledEnabled())
                .isHostRatedEnabled(hostNotificationSettings.getIsHostRatedEnabled())
                .isAccommodationRatedEnabled(hostNotificationSettings.getIsAccommodationRatedEnabled())
                .build();
    }
}
