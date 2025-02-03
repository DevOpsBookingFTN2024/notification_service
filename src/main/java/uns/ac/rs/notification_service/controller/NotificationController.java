package uns.ac.rs.notification_service.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.notification_service.dto.NotificationDTO;
import uns.ac.rs.notification_service.dto.request.CreateNotificationRequest;
import uns.ac.rs.notification_service.dto.response.MessageResponse;
import uns.ac.rs.notification_service.service.NotificationService;
import java.util.List;

@CrossOrigin(origins = "*")
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
        MessageResponse messageResponse = notificationService
                .createNotification(createNotificationRequest, jwtToken);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMyNotifications(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        List<NotificationDTO> notifications = notificationService.getAllMyNotifications(jwtToken);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getAllMyUnreadNotifications(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        List<NotificationDTO> notifications = notificationService.getAllMyUnreadNotifications(jwtToken);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/read/{notificationId}")
    public ResponseEntity<?> readNotification(@PathVariable String notificationId,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        MessageResponse messageResponse = notificationService.readNotification(notificationId, jwtToken);
        return ResponseEntity.ok(messageResponse);
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable String notificationId,
                                                @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        MessageResponse messageResponse = notificationService.deleteNotification(notificationId, jwtToken);
        return ResponseEntity.ok(messageResponse);
    }
}
