package uns.ac.rs.notification_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNotificationRequest {
    @NotBlank(message = "Recipient is required.")
    private String recipient;

    @NotBlank(message = "Message is required.")
    private String message;

    @NotBlank(message = "Type is required.")
    private String type;
}
