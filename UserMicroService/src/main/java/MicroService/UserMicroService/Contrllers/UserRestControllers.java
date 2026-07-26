package MicroService.UserMicroService.Contrllers;



import MicroService.UserMicroService.Dtos.CreateUserRequest;
import MicroService.UserMicroService.Dtos.ProfileSetupRequest;
import MicroService.UserMicroService.Dtos.UpdateUserRequest;
import MicroService.UserMicroService.Dtos.UserResponse;
import MicroService.UserMicroService.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestControllers {

    private final UserService userService;

    /**
     * Create a new user
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        UserResponse response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Complete user profile setup
     */
    @PutMapping("/{userUuid}/profile")
    public ResponseEntity<UserResponse> setupProfile(
            @PathVariable String userUuid,
            @Valid @RequestBody ProfileSetupRequest request) {

        UserResponse response =
                userService.setupProfile(userUuid, request);

        return ResponseEntity.ok(response);
    }

    /**
     * Update existing profile
     */
    @PutMapping("/{userUuid}")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable String userUuid,
            @Valid @RequestBody UpdateUserRequest request) {

        UserResponse response =
                userService.updateProfile(userUuid, request);

        return ResponseEntity.ok(response);
    }

    /**
     * Get user by UUID
     */
    @GetMapping("/{userUuid}")
    public ResponseEntity<UserResponse> getUserByUuid(
            @PathVariable String userUuid) {

        UserResponse response =
                userService.getUserByUuid(userUuid);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users =
                userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    /**
     * Delete user
     */
    @DeleteMapping("/{userUuid}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String userUuid) {

        userService.deleteUser(userUuid);

        return ResponseEntity.noContent().build();
    }



}
