package uns.ac.rs.notification_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;

    private String recipient;

    private String message;

    private ENotificationType type;

    private LocalDateTime dateTime;

    private Boolean isRead;

    public Notification(String recipient,
                        String message,
                        ENotificationType type) {
        this.recipient = recipient;
        this.message = message;
        this.type = type;
    }
}
