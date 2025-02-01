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
@Document(collection = "host_notification_settings")
public class HostNotificationSettings {
    @Id
    private String id;

    private String host;

    private Boolean isReservationRequestEnabled;

    private Boolean isReservationCanceledEnabled;

    private Boolean isHostRatedEnabled;

    private Boolean isAccommodationRatedEnabled;

    public HostNotificationSettings(String host,
                                    Boolean isReservationRequestEnabled,
                                    Boolean isReservationCanceledEnabled,
                                    Boolean isHostRatedEnabled,
                                    Boolean isAccommodationRatedEnabled) {
        this.host = host;
        this.isReservationRequestEnabled = isReservationRequestEnabled;
        this.isReservationCanceledEnabled = isReservationCanceledEnabled;
        this.isHostRatedEnabled = isHostRatedEnabled;
        this.isAccommodationRatedEnabled = isAccommodationRatedEnabled;
    }
}
