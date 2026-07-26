package MicroService.UserMicroService.Servicesimpols;




import MicroService.UserMicroService.Dtos.*;
import MicroService.UserMicroService.Dtos.UserResponse;
import MicroService.UserMicroService.Entitites.ENUMS.AccountStatus;
import MicroService.UserMicroService.Entitites.User;
import MicroService.UserMicroService.Exceptions.UserAlreadyExistsException;
import MicroService.UserMicroService.Exceptions.UserNotFoundException;
import MicroService.UserMicroService.Repositories.UserRepository;
import MicroService.UserMicroService.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists.");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        if (request.getPhoneNumber() != null &&
                !request.getPhoneNumber().isBlank() &&
                userRepository.existsByPhoneNumber(request.getPhoneNumber())) {

            throw new UserAlreadyExistsException("Phone number already exists.");
        }

        User user = modelMapper.map(request, User.class);

        user.setProfileCompleted(false);
        user.setAccountStatus(AccountStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserResponse.class);
    }

   // Add this import

    @Override
    public UserResponse setupProfile(String userUuid, ProfileSetupRequest request) {
        User user = userRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        // 1. Map other fields
        modelMapper.map(request, user);

        // 2. MANUALLY handle the image conversion (String -> byte[])
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(request.getProfileImage());
                user.setProfileImage(imageBytes);
            } catch (IllegalArgumentException e) {
                // Handle invalid base64 if necessary
            }
        }

        user.setProfileCompleted(true);
        User updatedUser = userRepository.save(user);

        // 3. Convert back for the response
        return convertToResponse(updatedUser);
    }

    // Create a helper method to handle the byte[] -> String conversion for the UI
    private UserResponse convertToResponse(User user) {
        UserResponse response = modelMapper.map(user, UserResponse.class);
        if (user.getProfileImage() != null) {
            response.setProfileImage(Base64.getEncoder().encodeToString(user.getProfileImage()));
        }
        return response;
    }

    @Override
    public UserResponse updateProfile(String userUuid,
                                      UpdateUserRequest request) {

        User user = userRepository.findByUserUuid(userUuid)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        modelMapper.map(request, user);

        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserResponse.class);
    }

    @Override
    public UserResponse getUserByUuid(String userUuid) {

        User user = userRepository.findByUserUuid(userUuid)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String userUuid) {

        User user = userRepository.findByUserUuid(userUuid)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        userRepository.delete(user);
    }

}
