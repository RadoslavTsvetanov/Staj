package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);
    Optional<User> findById(Long id);
    boolean existsByUsername(String username);
    //Optional<User> findByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);
    User findByCredentialsId(Long credentialsId);
}
