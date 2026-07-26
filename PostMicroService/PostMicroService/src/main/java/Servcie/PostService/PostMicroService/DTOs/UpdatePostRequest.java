package Servcie.PostService.PostMicroService.DTOs;


import Servcie.PostService.PostMicroService.Entities.ENUMS.MediaType;
import Servcie.PostService.PostMicroService.Entities.ENUMS.PostVisibility;
import lombok.Data;

import java.util.List;

@Data
public class UpdatePostRequest {

    private String caption;

    private List<String> mediaUrls;

    private MediaType mediaType;

    private PostVisibility visibility;

    private String location;

    private List<String> hashtags;

    private List<String> mentionedUsers;

}