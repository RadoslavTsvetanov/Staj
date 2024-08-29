package uk.gov.hmcts.reform.demo.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCache implements Cache {

    private final ValueOperations<String, List<String>> valueOperations;

    public RedisCache(RedisTemplate<String, List<String>> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public List<String> get(String key) {
        return valueOperations.get(key);
    }

    @Override
    public void put(String key, List<String> value) {
        valueOperations.set(key, value, 1, TimeUnit.DAYS); // Set TTL (time to live) for the cache entry
    }
}
