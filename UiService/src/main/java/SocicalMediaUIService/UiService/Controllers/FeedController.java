package SocicalMediaUIService.UiService.Controllers;

import SocicalMediaUIService.UiService.Dtos.CreatePostRequest;
import SocicalMediaUIService.UiService.ENUMS.ENUMS.PostVisibility;
import SocicalMediaUIService.UiService.ENUMS.ENUMS.mediaType;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;

import org.springframework.stereotype.Controller;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FeedController {

    private final RestTemplate restTemplate;


    @PostMapping(
            value = "/proxy/posts",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseBody
    public ResponseEntity<?> proxyCreatePost(

            @RequestParam("caption")
            String caption,

            @RequestParam("visibility")
            String visibility,

            @RequestParam("mediaType")
            String mediaTypeValue,

            @RequestParam("userUuid")
            String userUuid,

            @RequestParam(
                    value = "location",
                    required = false
            )
            String location,

            @RequestParam(
                    value = "hashtags",
                    required = false
            )
            List<String> hashtags,

            @RequestParam(
                    value = "mentionedUsers",
                    required = false
            )
            List<String> mentionedUsers,

            @RequestParam(
                    value = "files",
                    required = false
            )
            List<MultipartFile> files
    ) {

        try {

            // =====================================================
            // 1. CREATE POST DTO
            // =====================================================

            CreatePostRequest requestDto =
                    new CreatePostRequest();

            requestDto.setCaption(caption);

            requestDto.setUserUuid(userUuid);

            requestDto.setLocation(location);

            requestDto.setHashtags(hashtags);

            requestDto.setMentionedUsers(mentionedUsers);


            // =====================================================
            // 2. ENUM CONVERSION
            // =====================================================

            requestDto.setVisibility(
                    PostVisibility.valueOf(
                            visibility.trim().toUpperCase()
                    )
            );

            requestDto.setMediaType(
                    mediaType.valueOf(
                            mediaTypeValue.trim().toUpperCase()
                    )
            );


            // =====================================================
            // 3. CREATE MULTIPART BODY
            // =====================================================

            MultiValueMap<String, Object> multipartBody =
                    new LinkedMultiValueMap<>();


            // =====================================================
            // 4. JSON "post" PART
            // =====================================================

            HttpHeaders postHeaders =
                    new HttpHeaders();

            postHeaders.setContentType(
                    MediaType.APPLICATION_JSON
            );

            postHeaders.set(
                    "Content-Disposition",
                    "form-data; name=\"post\""
            );


            HttpEntity<CreatePostRequest> postPart =
                    new HttpEntity<>(
                            requestDto,
                            postHeaders
                    );


            multipartBody.add(
                    "post",
                    postPart
            );


            // =====================================================
            // 5. FILE PARTS
            // =====================================================

            if (files != null) {

                for (MultipartFile file : files) {

                    if (file == null || file.isEmpty()) {
                        continue;
                    }


                    ByteArrayResource resource =
                            new ByteArrayResource(
                                    file.getBytes()
                            ) {

                                @Override
                                public String getFilename() {

                                    return file.getOriginalFilename();

                                }
                            };


                    HttpHeaders fileHeaders =
                            new HttpHeaders();


                    if (file.getContentType() != null
                            && !file.getContentType().isBlank()) {

                        fileHeaders.setContentType(
                                MediaType.parseMediaType(
                                        file.getContentType()
                                )
                        );

                    } else {

                        fileHeaders.setContentType(
                                MediaType.APPLICATION_OCTET_STREAM
                        );
                    }


                    fileHeaders.set(
                            "Content-Disposition",
                            "form-data; name=\"mediaFiles\"; filename=\""
                                    + file.getOriginalFilename()
                                    + "\""
                    );


                    HttpEntity<ByteArrayResource> filePart =
                            new HttpEntity<>(
                                    resource,
                                    fileHeaders
                            );


                    multipartBody.add(
                            "mediaFiles",
                            filePart
                    );
                }
            }


            // =====================================================
            // 6. MAIN MULTIPART HEADERS
            // =====================================================

            HttpHeaders mainHeaders =
                    new HttpHeaders();

            mainHeaders.setContentType(
                    MediaType.MULTIPART_FORM_DATA
            );

            mainHeaders.setAccept(
                    List.of(
                            MediaType.APPLICATION_JSON,
                            MediaType.TEXT_PLAIN
                    )
            );


            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(
                            multipartBody,
                            mainHeaders
                    );


            // =====================================================
            // 7. CALL POST MICROSERVICE
            // =====================================================

            System.out.println("======================================");
            System.out.println("SENDING POST TO POST MICROSERVICE");
            System.out.println("User UUID: " + userUuid);
            System.out.println("Caption: " + caption);
            System.out.println("Visibility: " + visibility);
            System.out.println("Media Type: " + mediaTypeValue);
            System.out.println("Files: " +
                    (files == null ? 0 : files.size()));
            System.out.println("======================================");


            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            "http://localhost:8082/api/v1/posts",
                            request,
                            String.class
                    );


            // =====================================================
            // 8. RETURN RESPONSE
            // =====================================================

            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(response.getBody());


        } catch (HttpStatusCodeException e) {

            // =====================================================
            // DOWNSTREAM POST MICROSERVICE ERROR
            // =====================================================

            System.err.println(
                    "=========================================="
            );

            System.err.println(
                    "POST MICROSERVICE ERROR"
            );

            System.err.println(
                    "Status: " + e.getStatusCode()
            );

            System.err.println(
                    "Response: " +
                            e.getResponseBodyAsString()
            );

            System.err.println(
                    "=========================================="
            );


            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(
                            e.getResponseBodyAsString()
                    );


        } catch (Exception e) {

            // =====================================================
            // UI SERVICE ERROR
            // =====================================================

            e.printStackTrace();


            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            "UI Service error while creating post: "
                                    + e.getClass().getSimpleName()
                                    + " - "
                                    + e.getMessage()
                    );
        }
    }

    @GetMapping("/proxy/posts/user/{userUuid}")
    @ResponseBody
    public ResponseEntity<?> proxyGetPostsByUser(@PathVariable String userUuid) {
        try {
            // Calling the Post Microservice endpoint you provided: /api/v1/posts/user/{userUuid}
            ResponseEntity<List> response = restTemplate.getForEntity(
                    "http://localhost:8082/api/v1/posts/user/" + userUuid,
                    List.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching user posts: " + e.getMessage());
        }
    }


    @PutMapping(value = "/proxy/posts/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<?> proxyUpdatePost(
            @PathVariable String postId,
            @RequestParam("caption") String caption,
            @RequestParam("visibility") String visibility,
            @RequestParam("mediaType") String mediaTypeValue,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "hashtags", required = false) List<String> hashtags,
            @RequestParam(value = "mentionedUsers", required = false) List<String> mentionedUsers
    ) {
        try {
            MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();

            // 1. Prepare DTO
            java.util.Map<String, Object> requestDto = new java.util.HashMap<>();
            requestDto.put("caption", caption);
            requestDto.put("visibility", visibility.toUpperCase());
            requestDto.put("mediaType", mediaTypeValue.toUpperCase());
            requestDto.put("location", location);
            requestDto.put("hashtags", hashtags);
            requestDto.put("mentionedUsers", mentionedUsers);

            // 2. Wrap as JSON part
            HttpHeaders jsonHeaders = new HttpHeaders();
            jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<java.util.Map<String, Object>> postPart = new HttpEntity<>(requestDto, jsonHeaders);
            multipartBody.add("post", postPart);

            // 3. Main headers
            HttpHeaders mainHeaders = new HttpHeaders();
            mainHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            // CRITICAL: Explicitly ask for JSON back to avoid the Video conversion error
            mainHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartBody, mainHeaders);

            // 4. Use EXCHANGE instead of PUT to get the response back
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8082/api/v1/posts/" + postId,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class
            );

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpStatusCodeException e) {
            // If microservice returns 400/500, catch it and return its JSON
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("UI Proxy Error: " + e.getMessage());
        }
    }

    // Add this to get single post details for the edit form
    @GetMapping("/proxy/posts/{postId}")
    @ResponseBody
    public ResponseEntity<?> proxyGetPost(@PathVariable String postId) {
        return restTemplate.getForEntity("http://localhost:8082/api/v1/posts/" + postId, String.class);
    }
}