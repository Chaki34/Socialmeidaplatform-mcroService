package Servcie.PostService.PostMicroService.Serviceimpal;



import Servcie.PostService.PostMicroService.DTOs.CreatePostRequest;
import Servcie.PostService.PostMicroService.DTOs.PostResponse;
import Servcie.PostService.PostMicroService.DTOs.UpdatePostRequest;
import Servcie.PostService.PostMicroService.Entities.ENUMS.PostStatus;
import Servcie.PostService.PostMicroService.Entities.Post;
import Servcie.PostService.PostMicroService.Exceptions.ResourceNotFoundException;
import Servcie.PostService.PostMicroService.Repositories.PostRepository;
import Servcie.PostService.PostMicroService.Services.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final ModelMapper modelMapper;

    @Override
    public PostResponse createPost(
            CreatePostRequest request,
            List<MultipartFile> mediaFiles) {

        System.out.println("======================================");
        System.out.println("CREATE POST STARTED");
        System.out.println("Caption       : " + request.getCaption());
        System.out.println("User UUID     : " + request.getUserUuid());
        System.out.println("Visibility    : " + request.getVisibility());
        System.out.println("Media Type    : " + request.getMediaType());
        System.out.println("Location      : " + request.getLocation());
        System.out.println("Hashtags      : " + request.getHashtags());
        System.out.println("Mentioned     : " + request.getMentionedUsers());

        if (mediaFiles == null) {
            System.out.println("Media files   : NULL");
        } else {
            System.out.println("Media files   : " + mediaFiles.size());

            for (MultipartFile file : mediaFiles) {
                System.out.println(
                        "File          : "
                                + file.getOriginalFilename()
                                + " | "
                                + file.getContentType()
                                + " | "
                                + file.getSize()
                );
            }
        }

        try {

            // ==========================================
            // 1. MAP DTO -> ENTITY
            // ==========================================

            System.out.println("STEP 1: Mapping DTO to Post");

            Post post = modelMapper.map(request, Post.class);

            System.out.println("STEP 1 SUCCESS");


            // ==========================================
            // 2. SAVE FILES
            // ==========================================

            System.out.println("STEP 2: Saving files");

            List<String> urls = saveFiles(mediaFiles);

            System.out.println("STEP 2 SUCCESS");
            System.out.println("URLs: " + urls);

            post.setMediaUrls(urls);


            // ==========================================
            // 3. DEFAULT VALUES
            // ==========================================

            System.out.println("STEP 3: Setting default values");

            post.setLikeCount(0L);
            post.setCommentCount(0L);
            post.setShareCount(0L);
            post.setViewCount(0L);

            post.setEdited(false);
            post.setStatus(PostStatus.ACTIVE);

            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());

            System.out.println("STEP 3 SUCCESS");


            // ==========================================
            // 4. SAVE MONGODB
            // ==========================================

            System.out.println("STEP 4: Saving Post to MongoDB");

            Post savedPost = postRepository.save(post);

            System.out.println("STEP 4 SUCCESS");
            System.out.println("POST ID: " + savedPost.getPostId());


            // ==========================================
            // 5. MAP ENTITY -> RESPONSE
            // ==========================================

            System.out.println("STEP 5: Mapping Post -> PostResponse");

            PostResponse response =
                    modelMapper.map(savedPost, PostResponse.class);

            System.out.println("STEP 5 SUCCESS");
            System.out.println("CREATE POST SUCCESS");
            System.out.println("======================================");

            return response;

        } catch (Exception e) {

            System.err.println("======================================");
            System.err.println("CREATE POST FAILED");
            System.err.println("Exception: " + e.getClass().getName());
            System.err.println("Message  : " + e.getMessage());

            e.printStackTrace();

            System.err.println("======================================");

            throw e;
        }
    }
    @Override
    public PostResponse getPostById(String postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found with id : " + postId));

        return modelMapper.map(post, PostResponse.class);
    }

    @Override
    public List<PostResponse> getAllPosts() {

        return postRepository.findAll()
                .stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
                .toList();
    }

    @Override
    public List<PostResponse> getPostsByUser(String userUuid) {

        return postRepository.findByUserUuid(userUuid)
                .stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
                .toList();
    }

    @Override
    public PostResponse updatePost (String postId,
                                    UpdatePostRequest request,
                                    List<MultipartFile> mediaFiles){

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found with id : " + postId));

        post.setCaption(request.getCaption());

        if (mediaFiles != null && !mediaFiles.isEmpty()) {

            // Delete old images from uploads folder
            deleteFiles(post.getMediaUrls());

            // Save newly uploaded images
            List<String> urls = saveFiles(mediaFiles);

            // Update MongoDB with new URLs
            post.setMediaUrls(urls);
        }

        post.setMediaType(request.getMediaType());

        post.setVisibility(request.getVisibility());

        post.setLocation(request.getLocation());

        post.setHashtags(request.getHashtags());

        post.setMentionedUsers(request.getMentionedUsers());

        post.setEdited(true);

        post.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(post);

        return modelMapper.map(updatedPost, PostResponse.class);
    }

    @Override
    public void deletePost(String postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found with id : " + postId));

        // Delete images from disk
        deleteFiles(post.getMediaUrls());

        // Delete MongoDB document
        postRepository.delete(post);
    }


    private List<String> saveFiles(List<MultipartFile> files) {

        List<String> urls = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            return urls;
        }

        Path uploadPath = Paths.get("src/main/resources/uploads");

        try {

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : files) {

                String fileName =
                        UUID.randomUUID() + "_" + file.getOriginalFilename();

                Path path = uploadPath.resolve(fileName);

                Files.copy(file.getInputStream(),
                        path,
                        StandardCopyOption.REPLACE_EXISTING);

                urls.add("http://localhost:8082/uploads/" + fileName);

            }

        } catch (IOException e) {

            throw new RuntimeException("Unable to upload file.");

        }

        return urls;
    }

    private void deleteFiles(List<String> mediaUrls) {

        if (mediaUrls == null || mediaUrls.isEmpty()) {
            return;
        }

        Path uploadPath = Paths.get("src/main/resources/uploads");

        for (String url : mediaUrls) {

            try {

                String fileName = url.substring(url.lastIndexOf("/") + 1);

                Path filePath = uploadPath.resolve(fileName);

                Files.deleteIfExists(filePath);

            } catch (IOException e) {

                throw new RuntimeException("Unable to delete old file.");
            }
        }
    }

}
