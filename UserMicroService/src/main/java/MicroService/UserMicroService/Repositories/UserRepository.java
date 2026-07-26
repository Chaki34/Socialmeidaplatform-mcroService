package MicroService.UserMicroService.Repositories;


import MicroService.UserMicroService.Entitites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by UUID.
     */
    Optional<User> findByUserUuid(String userUuid);

    /**
     * Find user by email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if email already exists.
     */
    boolean existsByEmail(String email);

    /**
     * Check if username already exists.
     */
    boolean existsByUsername(String username);

    /**
     * Check if phone number already exists.
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Delete user using UUID.
     */
    void deleteByUserUuid(String userUuid);
}
