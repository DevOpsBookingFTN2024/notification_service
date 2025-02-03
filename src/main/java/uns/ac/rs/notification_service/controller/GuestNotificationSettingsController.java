package uns.ac.rs.notification_service.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.notification_service.dto.GuestNotificationSettingsDTO;
import uns.ac.rs.notification_service.dto.request.UpdateGuestNotificationSettingsRequest;
import uns.ac.rs.notification_service.dto.response.MessageResponse;
import uns.ac.rs.notification_service.service.GuestNotificationSettingsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/notifications/guest")
public class GuestNotificationSettingsController {
    @Autowired
    GuestNotificationSettingsService guestNotificationSettingsService;

    //endpoint koristi UserService
    @PostMapping("/create")
    public ResponseEntity<?> createGuestNotificationSettings(
            @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        MessageResponse messageResponse = guestNotificationSettingsService
                .createGuestNotificationSettings(jwtToken);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyGuestNotificationSettings(
            @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        GuestNotificationSettingsDTO guestNotificationSettings = guestNotificationSettingsService
                .getMyGuestNotificationSettings(jwtToken);
        return ResponseEntity.ok(guestNotificationSettings);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMyGuestNotificationSettings(
            @Valid @RequestBody UpdateGuestNotificationSettingsRequest updateGuestNotificationSettingsRequest,
            @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        MessageResponse messageResponse = guestNotificationSettingsService
                .updateMyGuestNotificationSettings(updateGuestNotificationSettingsRequest, jwtToken);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/has-enabled-notifications")
    public ResponseEntity<?> isGuestHasEnabledNotifications(
                             @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        boolean result = guestNotificationSettingsService.isGuestHasEnabledNotifications(jwtToken);
        return ResponseEntity.ok(result);
    }
}
