package SocicalMediaUIService.UiService.FingnClient;






import SocicalMediaUIService.UiService.Dtos.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "UserMicroService")
public interface UserFeignClient {


    /**
     * Create new user
     */
    @PostMapping("/api/v1/users")
    UserResponse createUser(
            @RequestBody CreateUserRequest request
    );


    /**
     * Complete user profile setup
     */
    @PutMapping("/api/v1/users/{userUuid}/profile")
    UserResponse setupProfile(
            @PathVariable("userUuid") String userUuid,
            @RequestBody ProfileSetupRequest request
    );


    /**
     * Update existing profile
     */
    @PutMapping("/api/v1/users/{userUuid}")
    UserResponse updateUser(
            @PathVariable("userUuid") String userUuid,
            @RequestBody UpdateUserRequest request
    );


    /**
     * Get user by UUID
     */
    @GetMapping("/api/v1/users/{userUuid}")
    UserResponse getUserByUuid(
            @PathVariable("userUuid") String userUuid
    );


    /**
     * Get all users
     */
    @GetMapping("/api/v1/users")
    List<UserResponse> getAllUsers();


    /**
     * Delete user
     */
    @DeleteMapping("/api/v1/users/{userUuid}")
    void deleteUser(
            @PathVariable("userUuid") String userUuid
    );

}