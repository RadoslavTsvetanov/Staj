package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.Credentials;

public interface CredentialsRepo extends JpaRepository<Credentials, Long> {
    Credentials findByUsername(String username);
}
