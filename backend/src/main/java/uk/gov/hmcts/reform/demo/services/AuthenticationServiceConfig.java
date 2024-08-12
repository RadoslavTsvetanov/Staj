package uk.gov.hmcts.reform.demo.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AuthenticationServiceConfig {

    @Bean
    public Map<String, AuthenticationService.TokenInfo> tokenStore() {
        return new ConcurrentHashMap<>();
    }
}
