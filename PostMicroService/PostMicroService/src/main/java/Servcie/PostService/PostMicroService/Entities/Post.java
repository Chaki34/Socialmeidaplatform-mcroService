package Servcie.PostService.PostMicroService.Entities;


import Servcie.PostService.PostMicroService.Entities.ENUMS.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "posts")
public class Post {

    @Id
    private String postId;

    // Owner of the post (from User Service)
    private String userUuid;

    // Post content
    private String caption;

    // Multiple images/videos
    private List<String> mediaUrls;

    // IMAGE, VIDEO, MIXED, TEXT
    private MediaType mediaType;

    // PUBLIC, FRIENDS, PRIVATE
    private PostVisibility visibility;

    // Optional location
    private String location;

    // Hashtags
    private List<String> hashtags;

    // Mentioned users (UUIDs)
    private List<String> mentionedUsers;

    // Statistics
    private Long likeCount;

    private Long commentCount;

    private Long shareCount;

    private Long viewCount;

    // Status
    private PostStatus status;

    // Edited?
    private Boolean edited;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}