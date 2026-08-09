package Servcie.PostService.PostMicroService.DTOs;



import Servcie.PostService.PostMicroService.Entities.ENUMS.PostVisibility;
import Servcie.PostService.PostMicroService.Entities.ENUMS.mediaType;
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