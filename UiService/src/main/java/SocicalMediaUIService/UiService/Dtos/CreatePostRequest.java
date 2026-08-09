package SocicalMediaUIService.UiService.Dtos;



import SocicalMediaUIService.UiService.ENUMS.ENUMS.PostVisibility;
import SocicalMediaUIService.UiService.ENUMS.ENUMS.mediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.List;

@Data
public class CreatePostRequest {

    @NotBlank(message = "User UUID is required")
    private String userUuid;

    private String caption;

    private List<String> mediaUrls;

    @NotNull(message = "Media type is required")
    private mediaType mediaType;

    @NotNull(message = "Visibility is required")
    private PostVisibility visibility;

    private String location;

    private List<String> hashtags;

    private List<String> mentionedUsers;



}