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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // Physical directory
    private final String UPLOAD_DIRECTORY = "D:/Microservices Using Springboot/Social meida platform webapp/src/main/resources/profile_images/";

    @Override
    @Transactional
    public UserResponse setupProfile(String userUuid, ProfileSetupRequest request) {
        User user = userRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        // 1. Save Image to Disk first
        String fileName = null;
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            fileName = saveImageToFileSystem(request.getProfileImage(), userUuid);
        }

        // 2. Map fields from request to user
        // Temporarily clear image in request so ModelMapper doesn't try to map Base64 string to the String filename field
        String tempBase64 = request.getProfileImage();
        request.setProfileImage(null);
        modelMapper.map(request, user);
        request.setProfileImage(tempBase64);

        // 3. Manually set the filename to the entity
        if (fileName != null) {
            user.setProfileImage(fileName);
        }

        user.setProfileCompleted(true);
        User updatedUser = userRepository.save(user);

        System.out.println("========== PROFILE REQUEST ==========");
        System.out.println("First Name : " + request.getFirstName());
        System.out.println("Image Null : " + (request.getProfileImage() == null));

        if (request.getProfileImage() != null) {
            System.out.println("Image Length : " + request.getProfileImage().length());
            System.out.println(request.getProfileImage().substring(0, 40));
        }
        System.out.println("=====================================");

        return convertToResponse(updatedUser);
    }

    private String saveImageToFileSystem(String base64Image, String userUuid) {
        try {
            File directory = new File(UPLOAD_DIRECTORY);
            if (!directory.exists()) directory.mkdirs();

            String base64Data = base64Image.contains(",") ? base64Image.split(",")[1] : base64Image;
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // Using just UUID for filename to keep it clean in DB
            String fileName = userUuid + ".png";
            Path filePath = Paths.get(UPLOAD_DIRECTORY + fileName);
            Files.write(filePath, imageBytes);

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image file: " + e.getMessage());
        }
    }

    private UserResponse convertToResponse(User user) {
        UserResponse response = modelMapper.map(user, UserResponse.class);
        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
            // Build the URL: http://localhost:8081/profile-images/uuid.png
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/profile-images/")
                    .path(user.getProfileImage())
                    .toUriString();
            response.setProfileImage(fileDownloadUri);
        }
        return response;
    }

    @Override
    public UserResponse getUserByUuid(String userUuid) {
        User user = userRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        return convertToResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ... implement other methods using convertToResponse(savedUser)
    @Override public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) throw new UserAlreadyExistsException("Email exists");
        User user = modelMapper.map(request, User.class);
        return convertToResponse(userRepository.save(user));
    }
    @Override public void deleteUser(String userUuid) { /* ... */ }
    @Override public UserResponse updateProfile(String u, UpdateUserRequest r) { /* ... */ return null; }
}