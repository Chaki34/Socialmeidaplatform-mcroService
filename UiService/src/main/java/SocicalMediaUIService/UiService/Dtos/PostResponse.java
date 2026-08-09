package SocicalMediaUIService.UiService.Dtos;



import SocicalMediaUIService.UiService.ENUMS.ENUMS.mediaType;
import SocicalMediaUIService.UiService.ENUMS.ENUMS.PostStatus;
import SocicalMediaUIService.UiService.ENUMS.ENUMS.PostVisibility;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponse {

    private String postId;

    private String userUuid;

    private String caption;

    private List<String> mediaUrls;

    private mediaType mediaType;

    private PostVisibility visibility;

    private String location;

    private List<String> hashtags;

    private List<String> mentionedUsers;

    private Long likeCount;

    private Long commentCount;

    private Long shareCount;

    private Long viewCount;

    private PostStatus status;

    private Boolean edited;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}