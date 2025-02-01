package uns.ac.rs.notification_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateHostNotificationSettingsRequest {
    @NotNull(message = "ReservationRequestEnabled status is required.")
    private Boolean isReservationRequestEnabled;

    @NotNull(message = "ReservationCanceledEnabled status is required.")
    private Boolean isReservationCanceledEnabled;

    @NotNull(message = "HostRatedEnabled status is required.")
    private Boolean isHostRatedEnabled;

    @NotNull(message = "AccommodationRatedEnabled status is required.")
    private Boolean isAccommodationRatedEnabled;
}
