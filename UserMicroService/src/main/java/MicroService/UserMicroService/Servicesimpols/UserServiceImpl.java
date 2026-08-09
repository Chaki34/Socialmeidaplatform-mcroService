package MicroService.UserMicroService.Servicesimpols;

import MicroService.UserMicroService.Dtos.*;

import MicroService.UserMicroService.Entitites.ENUMS.AccountStatus;
import MicroService.UserMicroService.Entitites.User;
import MicroService.UserMicroService.Exceptions.UserAlreadyExistsException;
import MicroService.UserMicroService.Exceptions.UserNotFoundException;
import MicroService.UserMicroService.Repositories.UserRepository;
import MicroService.UserMicroService.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // Physical Folder
    private static final String UPLOAD_DIRECTORY =
            "D:/Microservices Using Springboot/Social meida platform webapp/src/main/resources/profile_images/";





////    for use in phone use ip adress sothat phone  -> sent to here  - 10.58.200.219:8081/profile-images
//   private static final String IMAGE_URL =
//        "http://10.58.200.219:8081/profile-images/";




    // URL exposed by WebConfig
    private static final String IMAGE_URL =
            "http://localhost:8081/profile-images/";

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists.");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        User user = modelMapper.map(request, User.class);

        user.setProfileCompleted(false);
        user.setAccountStatus(AccountStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse setupProfile(String userUuid, ProfileSetupRequest request) {

        User user = userRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        modelMapper.map(request, user);

        if (request.getProfileImage() != null &&
                !request.getProfileImage().isBlank()) {

            String fileName = saveImage(request.getProfileImage(), userUuid);

            user.setProfileImage(fileName);
        }

        user.setProfileCompleted(true);

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    @Override
    public UserResponse updateProfile(String userUuid, UpdateUserRequest request) {

        User user = userRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        modelMapper.map(request, user);

        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        return convertToResponse(saved);
    }

    @Override
    public UserResponse getUserByUuid(String userUuid) {

        User user = userRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return convertToResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String userUuid) {

        User user = userRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getProfileImage() != null) {

            File image = new File(UPLOAD_DIRECTORY + user.getProfileImage());

            if (image.exists()) {
                image.delete();
            }
        }

        userRepository.delete(user);
    }

    // ---------------------------

    private String saveImage(String base64Image, String userUuid) {

        try {

            File folder = new File(UPLOAD_DIRECTORY);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String base64 = base64Image;

            if (base64.contains(",")) {
                base64 = base64.split(",")[1];
            }

            byte[] imageBytes = Base64.getDecoder().decode(base64);

            String fileName = userUuid + ".png";

            Path path = Paths.get(UPLOAD_DIRECTORY + fileName);

            Files.write(path, imageBytes);

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("Unable to save image.", e);
        }
    }

    private UserResponse convertToResponse(User user) {

        UserResponse response = modelMapper.map(user, UserResponse.class);

        if (user.getProfileImage() != null &&
                !user.getProfileImage().isBlank()) {

            response.setProfileImage(
                    IMAGE_URL + user.getProfileImage()
            );
        }

        return response;
    }

    // Inside MicroService.UserMicroService.Servicesimpols.UserServiceImpl

    @Override
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Invalid Username or Password"));

        // In a real app, use BCrypt. For now, we compare plain text as requested.
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid Username or Password");
        }

        return convertToResponse(user);
    }
}