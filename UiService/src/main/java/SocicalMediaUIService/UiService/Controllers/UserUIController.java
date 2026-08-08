package SocicalMediaUIService.UiService.Controllers;


import SocicalMediaUIService.UiService.Dtos.CreateUserRequest;
import SocicalMediaUIService.UiService.Dtos.ProfileSetupRequest;
import SocicalMediaUIService.UiService.Dtos.UpdateUserRequest;
import SocicalMediaUIService.UiService.Dtos.UserResponse;
import SocicalMediaUIService.UiService.FingnClient.UserFeignClient;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ui/users")
@RequiredArgsConstructor
public class UserUIController {


    private final UserFeignClient userFeignClient;



    /**
     * Create New User
     *
     * POST /ui/users
     *
     * Request:
     * {
     *   username,
     *   email,
     *   phoneNumber
     * }
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {

        UserResponse response =
                userFeignClient.createUser(request);

        return ResponseEntity.ok(response);
    }




    /**
     * Complete Profile Setup
     *
     * PUT /ui/users/{userUuid}/profile
     *
     * Request:
     * {
     *   firstName,
     *   lastName,
     *   bio,
     *   gender,
     *   dateOfBirth,
     *   country,
     *   city,
     *   profileImage
     * }
     */
    @PutMapping("/{userUuid}/profile")
    public ResponseEntity<UserResponse> setupProfile(
            @PathVariable String userUuid,
            @Valid @RequestBody ProfileSetupRequest request
    ) {


        UserResponse response =
                userFeignClient.setupProfile(
                        userUuid,
                        request
                );


        return ResponseEntity.ok(response);
    }





    /**
     * Get User By UUID
     *
     * GET /ui/users/{userUuid}
     *
     * Example:
     * GET /ui/users/8f92a7c1-xxxx
     */
    @GetMapping("/{userUuid}")
    public ResponseEntity<UserResponse> getUserByUuid(
            @PathVariable String userUuid
    ) {


        UserResponse response =
                userFeignClient.getUserByUuid(userUuid);


        return ResponseEntity.ok(response);
    }






    /**
     * Get All Users
     *
     * GET /ui/users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {


        List<UserResponse> users =
                userFeignClient.getAllUsers();


        return ResponseEntity.ok(users);
    }







    /**
     * Update Existing User
     *
     * PUT /ui/users/{userUuid}
     */
    @PutMapping("/{userUuid}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String userUuid,
            @Valid @RequestBody UpdateUserRequest request
    ) {


        UserResponse response =
                userFeignClient.updateUser(
                        userUuid,
                        request
                );


        return ResponseEntity.ok(response);
    }







    /**
     * Delete User
     *
     * DELETE /ui/users/{userUuid}
     */
    @DeleteMapping("/{userUuid}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String userUuid
    ) {


        userFeignClient.deleteUser(userUuid);


        return ResponseEntity.noContent().build();
    }

    // Inside SocicalMediaUIService.UiService.Controllers.UserUIController

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody Object request) {
        // This calls the microservice via Feign
        UserResponse response = userFeignClient.login(request);
        return ResponseEntity.ok(response);
    }



}