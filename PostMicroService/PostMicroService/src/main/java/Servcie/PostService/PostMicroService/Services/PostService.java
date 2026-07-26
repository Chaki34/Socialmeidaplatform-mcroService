package Servcie.PostService.PostMicroService.Services;



import Servcie.PostService.PostMicroService.DTOs.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    /**
     * Create a new post
     */
    PostResponse createPost(CreatePostRequest request,
                            List<MultipartFile> mediaFiles);

    /**
     * Get a post by its ID
     */
    PostResponse getPostById(String postId);

    /**
     * Get all posts
     */
    List<PostResponse> getAllPosts();

    /**
     * Get all posts created by a specific user
     */
    List<PostResponse> getPostsByUser(String userUuid);

    /**
     * Update an existing post
     */
    PostResponse updatePost(String postId,
                            UpdatePostRequest request,
                            List<MultipartFile> mediaFiles);

    /**
     * Delete a post
     */
    void deletePost(String postId);

}