package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    // Custom query method to find User by the username in Credentials
    User findByCredentials_Username(String username);
    Optional<User> findById(Long id);

}
