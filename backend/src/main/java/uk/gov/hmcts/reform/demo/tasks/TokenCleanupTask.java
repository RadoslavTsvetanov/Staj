package uk.gov.hmcts.reform.demo.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.demo.services.AuthenticationService;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;

@Component
public class TokenCleanupTask {

    private final Map<String, AuthenticationService.TokenInfo> tokenStore;

    public TokenCleanupTask(Map<String, AuthenticationService.TokenInfo> tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Scheduled(fixedRate = 3600000) // na chas
    public void cleanUpExpiredTokens() {
        Iterator<Map.Entry<String, AuthenticationService.TokenInfo>> iterator = tokenStore.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AuthenticationService.TokenInfo> entry = iterator.next();
            if (entry.getValue().expiresAt().isBefore(LocalDateTime.now())) {
                iterator.remove();
            }
        }
    }
}
