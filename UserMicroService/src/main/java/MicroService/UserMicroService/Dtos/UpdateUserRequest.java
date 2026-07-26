package MicroService.UserMicroService.Dtos;


import MicroService.UserMicroService.Entitites.ENUMS.Gender;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    @Size(max = 80)
    private String firstName;

    @Size(max = 80)
    private String lastName;

    @Size(max = 1000)
    private String bio;

    private String profileImage;

    private Gender gender;

    @Past
    private LocalDate dateOfBirth;

    private String country;

    private String state;

    private String city;

    private String language;
}
