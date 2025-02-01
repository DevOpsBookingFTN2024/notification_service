package uns.ac.rs.notification_service.mapper;

import uns.ac.rs.notification_service.dto.GuestNotificationSettingsDTO;
import uns.ac.rs.notification_service.model.GuestNotificationSettings;

public class GuestNotificationSettingsMapper {
    public static GuestNotificationSettingsDTO toGuestNotificationSettingsDTO(
                                               GuestNotificationSettings guestNotificationSettings) {
        return GuestNotificationSettingsDTO.builder()
                .id(guestNotificationSettings.getId())
                .guest(guestNotificationSettings.getGuest())
                .isReservationResponseEnabled(guestNotificationSettings.getIsReservationResponseEnabled())
                .build();
    }
}
