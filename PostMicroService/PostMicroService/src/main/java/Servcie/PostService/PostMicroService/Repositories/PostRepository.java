package Servcie.PostService.PostMicroService.Repositories;


import Servcie.PostService.PostMicroService.Entities.*;
import Servcie.PostService.PostMicroService.Entities.ENUMS.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    /**
     * Get all posts of a specific user.
     */
    List<Post> findByUserUuid(String userUuid);

    /**
     * Get all posts with a specific visibility.
     */
    List<Post> findByVisibility(PostVisibility visibility);

    /**
     * Get all posts with a specific status.
     */
    List<Post> findByStatus(PostStatus status);

    /**
     * Get all posts of a user with a specific status.
     */
    List<Post> findByUserUuidAndStatus(String userUuid, PostStatus status);

    /**
     * Search posts by hashtag.
     */
    List<Post> findByHashtagsContaining(String hashtag);

    /**
     * Search posts by mentioned user.
     */
    List<Post> findByMentionedUsersContaining(String userUuid);

}