package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

        if (credentials != null && credentials.getPassword().equals(password)) {
            String token = UUID.randomUUID().toString();

            tokenStore.put(token, new TokenInfo(credentials, LocalDateTime.now().plusHours(2)));
            return token;
        }
        throw new RuntimeException("Invalid email or password");
    }

    public Credentials validateToken(String token) {
        TokenInfo tokenInfo = tokenStore.get(token);

        if (tokenInfo != null && tokenInfo.getExpiresAt().isAfter(LocalDateTime.now())) {
            return tokenInfo.getCredentials();
        }
        throw new RuntimeException("Invalid or expired token");
    }

    public void logout(String token) {
        tokenStore.remove(token);
    }

    public static class TokenInfo {
        private final Credentials credentials;
        private final LocalDateTime expiresAt;

        public TokenInfo(Credentials credentials, LocalDateTime expiresAt) {
            this.credentials = credentials;
            this.expiresAt = expiresAt;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public LocalDateTime getExpiresAt() {
            return expiresAt;
        }
    }
}

