package SocicalMediaUIService.UiService.Controllers;

import SocicalMediaUIService.UiService.Dtos.CreateUserRequest;
import SocicalMediaUIService.UiService.Dtos.ProfileSetupRequest;
import SocicalMediaUIService.UiService.Dtos.UserResponse;
import SocicalMediaUIService.UiService.FingnClient.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ui/users")
@RequiredArgsConstructor
public class UserUIController {

    private final UserFeignClient userFeignClient;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request){
        return ResponseEntity.ok(userFeignClient.createUser(request));
    }

    @PutMapping("/{userUuid}/profile")
    public ResponseEntity<UserResponse> setupProfile(
            @PathVariable String userUuid,
            @RequestBody ProfileSetupRequest request){
        return ResponseEntity.ok(userFeignClient.setupProfile(userUuid, request));
    }

    @GetMapping("/{userUuid}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userUuid) {
        return ResponseEntity.ok(userFeignClient.getUserByUuid(userUuid));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userFeignClient.getAllUsers());
    }

    @DeleteMapping("/{userUuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userUuid){
        userFeignClient.deleteUser(userUuid);
        return ResponseEntity.noContent().build();
    }
}