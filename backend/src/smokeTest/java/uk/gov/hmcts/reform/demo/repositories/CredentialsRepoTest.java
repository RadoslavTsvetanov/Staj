package uk.gov.hmcts.reform.demo.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import uk.gov.hmcts.reform.demo.models.Credentials;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CredentialsRepoTest {

    @Autowired
    private CredentialsRepo credentialsRepo;

    private Credentials testCredentials;

    @BeforeEach
    public void setUp() {
        testCredentials = new Credentials();
        testCredentials.setEmail("test@example.com");
        testCredentials.setPassword("securepassword");
        credentialsRepo.save(testCredentials);
    }

    @Test
    public void testFindByEmail() {
        Credentials found = credentialsRepo.findByEmail("test@example.com");
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testExistsByEmail() {
        boolean exists = credentialsRepo.existsByEmail("test@example.com");
        assertThat(exists).isTrue();

        exists = credentialsRepo.existsByEmail("nonexistent@example.com");
        assertThat(exists).isFalse();
    }
}
