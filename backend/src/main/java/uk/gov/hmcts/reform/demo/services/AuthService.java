package uk.gov.hmcts.reform.demo.services;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.mocks.TokenProviderMocker;

@Service
public class AuthService {

    private final TokenProviderMocker tokenProvider;

    public AuthService() {
        this.tokenProvider = new TokenProviderMocker();
    }

    public String issueToken(String id) {
        return tokenProvider.provideToken(id);
    }

    public boolean checkToken(String token, String id) {
        String subject = tokenProvider.verifyToken(token);
        return id.equals(subject);
    }

    public boolean checkIfTokenExists(String token) {
        return tokenProvider.verifyToken(token) != null;
    }
}
