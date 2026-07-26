package MicroService.UserMicroService.Dtos;


import MicroService.UserMicroService.Entitites.ENUMS.AccountStatus;
import MicroService.UserMicroService.Entitites.ENUMS.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String userUuid;

    private String email;

    private String phoneNumber;

    private String username;

    private String firstName;

    private String lastName;

    private String bio;

    // This will send the Base64 string back to the UI for display
    private String profileImage;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String country;

    private String state;

    private String city;

    private String language;

    private Boolean profileCompleted;

    private AccountStatus accountStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}