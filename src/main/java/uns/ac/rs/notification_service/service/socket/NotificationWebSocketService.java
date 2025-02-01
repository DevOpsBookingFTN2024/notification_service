package uns.ac.rs.notification_service.service.socket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uns.ac.rs.notification_service.dto.NotificationDTO;

@Service
public class NotificationWebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(NotificationDTO notification) {
        messagingTemplate.convertAndSend("/topic/notifications/"
                + notification.getRecipient(), notification);
    }
}
