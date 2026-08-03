package SocicalMediaUIService.UiService.FingnClient;


import SocicalMediaUIService.UiService.Dtos.CreateUserRequest;
import SocicalMediaUIService.UiService.Dtos.ProfileSetupRequest;
import SocicalMediaUIService.UiService.Dtos.UpdateUserRequest;
import SocicalMediaUIService.UiService.Dtos.UserResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "UserMicroService")
public interface UserFeignClient {



    /*
       Create User

       POST
       /api/v1/users
    */
    @PostMapping("/api/v1/users")
    UserResponse createUser(
            @RequestBody CreateUserRequest request
    );





    /*
       Complete Profile

       PUT
       /api/v1/users/{uuid}/profile
    */
    @PutMapping("/api/v1/users/{userUuid}/profile")
    UserResponse setupProfile(
            @PathVariable("userUuid") String userUuid,
            @RequestBody ProfileSetupRequest request
    );





    /*
       Get User By UUID

       GET
       /api/v1/users/{uuid}

    */
    @GetMapping("/api/v1/users/{userUuid}")
    UserResponse getUserByUuid(
            @PathVariable("userUuid") String userUuid
    );





    /*
       Get All Users

       GET
       /api/v1/users

    */
    @GetMapping("/api/v1/users")
    List<UserResponse> getAllUsers();






    /*
       Update User

       PUT
       /api/v1/users/{uuid}

    */
    @PutMapping("/api/v1/users/{userUuid}")
    UserResponse updateUser(
            @PathVariable("userUuid") String userUuid,
            @RequestBody UpdateUserRequest request
    );






    /*
       Delete User

       DELETE
       /api/v1/users/{uuid}

    */
    @DeleteMapping("/api/v1/users/{userUuid}")
    void deleteUser(
            @PathVariable("userUuid") String userUuid
    );

}