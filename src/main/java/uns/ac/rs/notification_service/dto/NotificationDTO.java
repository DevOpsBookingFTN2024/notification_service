package uns.ac.rs.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String id;

    private String recipient;

    private String message;

    private String type;

    private LocalDateTime dateTime;

    private Boolean isRead;
}
