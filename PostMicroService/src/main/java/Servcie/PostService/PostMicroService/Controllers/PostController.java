package Servcie.PostService.PostMicroService.Controllers;


import Servcie.PostService.PostMicroService.DTOs.CreatePostRequest;
import Servcie.PostService.PostMicroService.DTOs.PostResponse;
import Servcie.PostService.PostMicroService.DTOs.UpdatePostRequest;
import Servcie.PostService.PostMicroService.Services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * Create a new Post
     */

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PostResponse> createPost(

            @RequestPart("post")
            @Valid
            CreatePostRequest request,

            @RequestPart(value = "mediaFiles", required = false)
            List<MultipartFile> mediaFiles) {

        PostResponse response =
                postService.createPost(request, mediaFiles);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    /**
     * Get Post by ID
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(
            @PathVariable String postId) {

        return ResponseEntity.ok(postService.getPostById(postId));
    }

    /**
     * Get all Posts
     */
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {

        return ResponseEntity.ok(postService.getAllPosts());
    }

    /**
     * Get all Posts of a User
     */
    @GetMapping("/user/{userUuid}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(
            @PathVariable String userUuid) {

        return ResponseEntity.ok(postService.getPostsByUser(userUuid));
    }

    /**
     * Update a Post
     */
    @PutMapping(value = "/{postId}", consumes = "multipart/form-data")
    public ResponseEntity<PostResponse> updatePost(

            @PathVariable String postId,

            @RequestPart("post")
            UpdatePostRequest request,

            @RequestPart(value = "mediaFiles", required = false)
            List<MultipartFile> mediaFiles) {

        return ResponseEntity.ok(
                postService.updatePost(postId, request, mediaFiles)
        );
    }

    /**
     * Delete a Post
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable String postId) {

        postService.deletePost(postId);

        return ResponseEntity.ok("Post deleted successfully.");
    }

}