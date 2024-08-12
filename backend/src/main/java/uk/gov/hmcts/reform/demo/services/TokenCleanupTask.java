package uk.gov.hmcts.reform.demo.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;

@Component
public class TokenCleanupTask {

    private final Map<String, AuthenticationService.TokenInfo> tokenStore;

    public TokenCleanupTask(Map<String, AuthenticationService.TokenInfo> tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Scheduled(fixedRate = 3600000) // Every hour
    public void cleanUpExpiredTokens() {
        Iterator<Map.Entry<String, AuthenticationService.TokenInfo>> iterator = tokenStore.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AuthenticationService.TokenInfo> entry = iterator.next();
            if (entry.getValue().getExpiresAt().isBefore(LocalDateTime.now())) {
                iterator.remove();
            }
        }
    }
}
