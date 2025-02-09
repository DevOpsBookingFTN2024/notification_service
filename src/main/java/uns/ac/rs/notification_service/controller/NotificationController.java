package uns.ac.rs.notification_service.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.notification_service.dto.NotificationDTO;
import uns.ac.rs.notification_service.dto.request.CreateNotificationRequest;
import uns.ac.rs.notification_service.dto.response.MessageResponse;
import uns.ac.rs.notification_service.service.NotificationService;
import java.util.List;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    //endpoint koriste ReservationService i RatingService
    @PostMapping("/create")
    public ResponseEntity<?> createNotification(@Valid @RequestBody CreateNotificationRequest createNotificationRequest,
                                                @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Creating notification for recipient: {}", createNotificationRequest.getRecipient());
        MessageResponse messageResponse = notificationService
                .createNotification(createNotificationRequest, jwtToken);
        log.info("Notification created successfully");
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMyNotifications(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Fetching all notifications for user.");
        List<NotificationDTO> notifications = notificationService.getAllMyNotifications(jwtToken);
        log.info("Total notifications fetched: {}", notifications.size());
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getAllMyUnreadNotifications(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Fetching unread notifications for user.");
        List<NotificationDTO> notifications = notificationService.getAllMyUnreadNotifications(jwtToken);
        log.info("Total unread notifications fetched: {}", notifications.size());
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/read-unread")
    public ResponseEntity<?> readAllMyUnreadNotifications(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Reading all unread notifications.");
        MessageResponse messageResponse = notificationService.readAllMyUnreadNotifications(jwtToken);
        log.info("Unread notifications have been read.");
        return ResponseEntity.ok(messageResponse);
    }

    @PutMapping("/read/{notificationId}")
    public ResponseEntity<?> readNotification(@PathVariable String notificationId,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Reading notification with ID: {}", notificationId);
        MessageResponse messageResponse = notificationService.readNotification(notificationId, jwtToken);
        log.info("Notification with ID: {} have been read.", notificationId);
        return ResponseEntity.ok(messageResponse);
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable String notificationId,
                                                @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Deleting notification with ID: {}", notificationId);
        MessageResponse messageResponse = notificationService.deleteNotification(notificationId, jwtToken);
        log.info("Notification with ID: {} have been deleted.", notificationId);
        return ResponseEntity.ok(messageResponse);
    }
}
