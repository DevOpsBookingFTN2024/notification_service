package uns.ac.rs.notification_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uns.ac.rs.notification_service.model.GuestNotificationSettings;

public interface GuestNotificationSettingsRepository extends MongoRepository<GuestNotificationSettings, String> {
    GuestNotificationSettings findByGuest(String guest);

    boolean existsByGuest(String guest);
}
