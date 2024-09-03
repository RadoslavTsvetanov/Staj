package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisCache implements Cache {

    private final RedisTemplate<String, List<String>> redisTemplate;

    @Autowired
    public RedisCache(RedisTemplate<String, List<String>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<String> get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void put(String key, List<String> value) {
        redisTemplate.opsForValue().set(key, value);
    }
}
