package uns.ac.rs.notification_service.mapper;

import uns.ac.rs.notification_service.dto.NotificationDTO;
import uns.ac.rs.notification_service.model.Notification;

public class NotificationMapper {
    public static NotificationDTO toNotificationDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .recipient(notification.getRecipient())
                .message(notification.getMessage())
                .type(notification.getType().name())
                .dateTime(notification.getDateTime())
                .isRead(notification.getIsRead())
                .build();
    }
}
