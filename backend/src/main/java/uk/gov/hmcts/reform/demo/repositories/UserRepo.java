package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);
    //User findByCredentials_Email(String email);
    Optional<User> findById(Long id);

}
