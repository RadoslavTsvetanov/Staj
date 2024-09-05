//package uk.gov.hmcts.reform.demo.config;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ApplicationContext;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//class RedisConfigTest {
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Test
//    void testJedisConnectionFactoryBean() {
//        JedisConnectionFactory jedisConnectionFactory = applicationContext.getBean(JedisConnectionFactory.class);
//        assertNotNull(jedisConnectionFactory);
//    }
//
//    @Test
//    void testRedisTemplateBean() {
//        RedisTemplate<String, List<String>> redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);
//        assertNotNull(redisTemplate);
//    }
//}
