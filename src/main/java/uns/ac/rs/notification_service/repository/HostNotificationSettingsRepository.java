package uns.ac.rs.notification_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uns.ac.rs.notification_service.model.HostNotificationSettings;

public interface HostNotificationSettingsRepository extends MongoRepository<HostNotificationSettings, String> {
    HostNotificationSettings findByHost(String host);

    boolean existsByHost(String host);
}
