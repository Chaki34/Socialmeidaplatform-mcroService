package SocicalMediaUIService.UiService.Dtos;



import SocicalMediaUIService.UiService.ENUMS.ENUMS.*;
import lombok.Data;

import java.util.List;

@Data
public class UpdatePostRequest {

    private String caption;

    private List<String> mediaUrls;

    private mediaType mediaType;

    private PostVisibility visibility;

    private String location;

    private List<String> hashtags;

    private List<String> mentionedUsers;

}