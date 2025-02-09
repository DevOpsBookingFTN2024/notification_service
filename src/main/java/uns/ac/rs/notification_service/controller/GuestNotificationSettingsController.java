package uns.ac.rs.notification_service.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.notification_service.dto.GuestNotificationSettingsDTO;
import uns.ac.rs.notification_service.dto.request.UpdateGuestNotificationSettingsRequest;
import uns.ac.rs.notification_service.dto.response.MessageResponse;
import uns.ac.rs.notification_service.service.GuestNotificationSettingsService;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/notifications/guest")
public class GuestNotificationSettingsController {
    @Autowired
    GuestNotificationSettingsService guestNotificationSettingsService;

    //endpoint koristi UserService
    @PostMapping("/create/{guest}")
    public ResponseEntity<?> createGuestNotificationSettings(@PathVariable String guest) {
        log.info("Creating notification settings for guest: {}", guest);
        MessageResponse messageResponse = guestNotificationSettingsService
                .createGuestNotificationSettings(guest);
        log.info("Notification settings created for guest: {}", guest);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyGuestNotificationSettings(
            @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Fetching guest notification settings for current user.");
        GuestNotificationSettingsDTO guestNotificationSettings = guestNotificationSettingsService
                .getMyGuestNotificationSettings(jwtToken);
        log.info("Guest notification settings retrieved.");
        return ResponseEntity.ok(guestNotificationSettings);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMyGuestNotificationSettings(
            @Valid @RequestBody UpdateGuestNotificationSettingsRequest updateGuestNotificationSettingsRequest,
            @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Updating guest notification settings for current user.");
        MessageResponse messageResponse = guestNotificationSettingsService
                .updateMyGuestNotificationSettings(updateGuestNotificationSettingsRequest, jwtToken);
        log.info("Guest notification settings updated.");
        return ResponseEntity.ok(messageResponse);
    }
}
