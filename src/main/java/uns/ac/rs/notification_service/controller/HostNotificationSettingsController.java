package uns.ac.rs.notification_service.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.notification_service.dto.HostNotificationSettingsDTO;
import uns.ac.rs.notification_service.dto.request.UpdateHostNotificationSettingsRequest;
import uns.ac.rs.notification_service.dto.response.MessageResponse;
import uns.ac.rs.notification_service.service.HostNotificationSettingsService;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/notifications/host")
public class HostNotificationSettingsController {
    @Autowired
    HostNotificationSettingsService hostNotificationSettingsService;

    //endpoint koristi UserService
    @PostMapping("/create/{host}")
    public ResponseEntity<?> createHostNotificationSettings(@PathVariable String host) {
        log.info("Creating notification settings for host: {}", host);
        MessageResponse messageResponse = hostNotificationSettingsService
                .createHostNotificationSettings(host);
        log.info("Notification settings created for host: {}", host);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyHostNotificationSettings(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Fetching host notification settings for current user.");
        HostNotificationSettingsDTO hostNotificationSettings = hostNotificationSettingsService
                .getMyHostNotificationSettings(jwtToken);
        log.info("Host notification settings retrieved.");
        return ResponseEntity.ok(hostNotificationSettings);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMyHostNotificationSettings(
            @Valid @RequestBody UpdateHostNotificationSettingsRequest updateHostNotificationSettingsRequest,
            @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Updating host notification settings for current user.");
        MessageResponse messageResponse = hostNotificationSettingsService
                .updateMyHostNotificationSettings(updateHostNotificationSettingsRequest, jwtToken);
        log.info("Host notification settings updated.");
        return ResponseEntity.ok(messageResponse);
    }
}
