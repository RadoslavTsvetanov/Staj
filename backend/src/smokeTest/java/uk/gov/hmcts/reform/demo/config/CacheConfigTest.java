package uk.gov.hmcts.reform.demo.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.hmcts.reform.demo.services.Cache;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = CacheConfig.class)
class CacheConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private RedisTemplate<String, List<String>> redisTemplate;

    @Test
    void testRedisCacheBean() {
        Cache redisCache = applicationContext.getBean("redisCacheConfig", Cache.class);
        assertNotNull(redisCache);
    }
}
