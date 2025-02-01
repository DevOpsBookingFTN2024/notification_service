package uns.ac.rs.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestNotificationSettingsDTO {
    private String id;

    private String guest;

    private Boolean isReservationResponseEnabled;
}
