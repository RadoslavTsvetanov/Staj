package uk.gov.hmcts.reform.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.exceptions.DuplicateEmailException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CredentialsServiceTest {

    @Mock
    private CredentialsRepo credentialsRepo;

    @InjectMocks
    private CredentialsService credentialsService;

    private Credentials testCredentials;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testCredentials = new Credentials();
        testCredentials.setEmail("test@example.com");
        testCredentials.setPassword("securepassword");
    }

    @Test
    public void testFindByEmail() {
        when(credentialsRepo.findByEmail("test@example.com")).thenReturn(testCredentials);

        Credentials found = credentialsService.findByEmail("test@example.com");

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("test@example.com");

        verify(credentialsRepo, times(1)).findByEmail("test@example.com");
    }

    @Test
    public void testSaveSuccess() {
        when(credentialsRepo.existsByEmail("test@example.com")).thenReturn(false);
        when(credentialsRepo.save(testCredentials)).thenReturn(testCredentials);

        Credentials saved = credentialsService.save(testCredentials);

        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("test@example.com");

        verify(credentialsRepo, times(1)).existsByEmail("test@example.com");
        verify(credentialsRepo, times(1)).save(testCredentials);
    }

    @Test
    public void testSaveDuplicateEmail() {
        when(credentialsRepo.existsByEmail("test@example.com")).thenReturn(true);

        DuplicateEmailException thrown = assertThrows(DuplicateEmailException.class, () -> {
            credentialsService.save(testCredentials);
        });

        assertThat(thrown.getMessage()).isEqualTo("Email test@example.com already exists");

        verify(credentialsRepo, times(1)).existsByEmail("test@example.com");
        verify(credentialsRepo, never()).save(any(Credentials.class));
    }

    @Test
    public void testFindAll() {
        when(credentialsRepo.findAll()).thenReturn(Collections.singletonList(testCredentials));

        List<Credentials> credentialsList = credentialsService.findAll();

        assertThat(credentialsList).isNotEmpty();
        assertThat(credentialsList.size()).isEqualTo(1);
        assertThat(credentialsList.get(0).getEmail()).isEqualTo("test@example.com");

        verify(credentialsRepo, times(1)).findAll();
    }
}
