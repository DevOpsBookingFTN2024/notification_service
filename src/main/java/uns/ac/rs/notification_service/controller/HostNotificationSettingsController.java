package uns.ac.rs.notification_service.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.notification_service.dto.HostNotificationSettingsDTO;
import uns.ac.rs.notification_service.dto.request.UpdateHostNotificationSettingsRequest;
import uns.ac.rs.notification_service.dto.response.MessageResponse;
import uns.ac.rs.notification_service.service.HostNotificationSettingsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/notifications/host")
public class HostNotificationSettingsController {
    @Autowired
    HostNotificationSettingsService hostNotificationSettingsService;

    //endpoint koristi UserService
    @PostMapping("/create/{host}")
    public ResponseEntity<?> createHostNotificationSettings(@PathVariable String host) {
        MessageResponse messageResponse = hostNotificationSettingsService
                .createHostNotificationSettings(host);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyHostNotificationSettings(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        HostNotificationSettingsDTO hostNotificationSettings = hostNotificationSettingsService
                .getMyHostNotificationSettings(jwtToken);
        return ResponseEntity.ok(hostNotificationSettings);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMyHostNotificationSettings(
            @Valid @RequestBody UpdateHostNotificationSettingsRequest updateHostNotificationSettingsRequest,
            @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        MessageResponse messageResponse = hostNotificationSettingsService
                .updateMyHostNotificationSettings(updateHostNotificationSettingsRequest, jwtToken);
        return ResponseEntity.ok(messageResponse);
    }
}
