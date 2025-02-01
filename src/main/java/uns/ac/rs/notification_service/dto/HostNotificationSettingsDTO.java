package uns.ac.rs.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostNotificationSettingsDTO {
    private String id;

    private String host;

    private Boolean isReservationRequestEnabled;

    private Boolean isReservationCanceledEnabled;

    private Boolean isHostRatedEnabled;

    private Boolean isAccommodationRatedEnabled;
}
