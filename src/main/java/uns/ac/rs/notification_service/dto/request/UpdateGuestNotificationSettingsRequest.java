package uns.ac.rs.notification_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateGuestNotificationSettingsRequest {
    @NotNull(message = "ReservationResponseEnabled status is required.")
    private Boolean isReservationResponseEnabled;
}
