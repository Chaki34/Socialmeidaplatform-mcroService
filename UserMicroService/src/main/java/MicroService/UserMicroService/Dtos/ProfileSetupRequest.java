package MicroService.UserMicroService.Dtos;



import MicroService.UserMicroService.Entitites.ENUMS.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSetupRequest {

    @NotBlank
    @Size(max = 80)
    private String firstName;

    @NotBlank
    @Size(max = 80)
    private String lastName;

    @Size(max = 1000)
    private String bio;

    // This will receive the Base64 string from the UI
    private String profileImage;

    private Gender gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private String country;

    private String state;

    private String city;

    private String language;
}
