package uk.gov.hmcts.reform.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.demo.services.Cache;
import uk.gov.hmcts.reform.demo.services.InMemoryCache;

@Configuration
public class CacheConfig {

    @Bean
    public Cache cache() {
        return new InMemoryCache();
    }
}
