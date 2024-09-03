//package uk.gov.hmcts.reform.demo.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class RedisCacheTest {
//
//    @Mock
//    private RedisTemplate<String, List<String>> redisTemplate;
//
//    @Mock
//    private ValueOperations<String, List<String>> valueOperations;
//
//    @InjectMocks
//    private RedisCache redisCache;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//    }
//
//    @Test
//    void testGet() {
//        String key = "libraries";
//        List<String> expectedValue = List.of("library");
//        when(valueOperations.get(key)).thenReturn(expectedValue);
//
//        List<String> result = redisCache.get(key);
//
//        assertEquals(expectedValue, result);
//        verify(valueOperations, times(1)).get(key);
//    }
//
//    @Test
//    void testPut() {
//        String key = "libraries";
//        List<String> value = List.of("library");
//
//        redisCache.put(key, value);
//
//        verify(valueOperations, times(1)).set(key, value);
//    }
//}
