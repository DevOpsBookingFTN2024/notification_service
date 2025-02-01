package uns.ac.rs.notification_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uns.ac.rs.notification_service.model.Notification;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByRecipient(String recipient);
}
