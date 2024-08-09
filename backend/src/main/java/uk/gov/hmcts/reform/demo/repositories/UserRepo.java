package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.demo.models.User;


public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
