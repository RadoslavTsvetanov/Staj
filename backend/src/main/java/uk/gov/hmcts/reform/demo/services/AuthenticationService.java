package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final Map<String, TokenInfo> tokenStore;

    @Autowired
    public AuthenticationService(Map<String, TokenInfo> tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Autowired
    private CredentialsRepo credentialsRepo;

    public String authenticate(String email, String password) {
        Credentials credentials = credentialsRepo.findByEmail(email);
        if (credentials != null) {
            System.out.println("Retrieved credentials: " + credentials.getEmail());
            if (credentials.getPassword().equals(password)) {
                String token = UUID.randomUUID().toString();
                tokenStore.put(token, new TokenInfo(credentials, LocalDateTime.now().plusHours(2)));
                return token;
            } else {
                System.out.println("Password mismatch");
            }
        } else {
            System.out.println("No credentials found for email: " + email);
        }
        throw new RuntimeException("Invalid email or password");
    }


    public void validateToken(String token) {
        TokenInfo tokenInfo = tokenStore.get(token);

        if (tokenInfo != null && tokenInfo.expiresAt().isAfter(LocalDateTime.now())) {
            return;
        }
        throw new RuntimeException("Invalid or expired token!");
    }

    public void logout(String token) {
        tokenStore.remove(token);
    }

    public record TokenInfo(Credentials credentials, LocalDateTime expiresAt) {
    }
}

