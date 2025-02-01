package uns.ac.rs.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "guest_notification_settings")
public class GuestNotificationSettings {
    @Id
    private String id;

    private String guest;

    private Boolean isReservationResponseEnabled;

    public GuestNotificationSettings(String guest,
                                     Boolean isReservationResponseEnabled) {
        this.guest = guest;
        this.isReservationResponseEnabled = isReservationResponseEnabled;
    }
}
