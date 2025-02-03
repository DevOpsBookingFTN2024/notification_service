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
    @PostMapping("/create/{guest}")
    public ResponseEntity<?> createGuestNotificationSettings(@PathVariable String guest) {
        MessageResponse messageResponse = guestNotificationSettingsService
                .createGuestNotificationSettings(guest);
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
}
