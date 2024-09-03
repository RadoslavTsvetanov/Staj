package uk.gov.hmcts.reform.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.demo.services.Cache;
import uk.gov.hmcts.reform.demo.services.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
public class CacheConfig {

    @Bean(name = "redisCache")
    public Cache cache(RedisTemplate<String, List<String>> redisTemplate) {
        return new RedisCache(redisTemplate);
    }
}
