package MicroService.UserMicroService.Services;




import MicroService.UserMicroService.Dtos.CreateUserRequest;
import MicroService.UserMicroService.Dtos.ProfileSetupRequest;
import MicroService.UserMicroService.Dtos.UpdateUserRequest;
import MicroService.UserMicroService.Dtos.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse setupProfile(String userUuid, ProfileSetupRequest request);

    UserResponse updateProfile(String userUuid, UpdateUserRequest request);

    UserResponse getUserByUuid(String userUuid);

    List<UserResponse> getAllUsers();

    void deleteUser(String userUuid);

}
