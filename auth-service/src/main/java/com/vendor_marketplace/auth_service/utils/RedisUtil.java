package com.vendor_marketplace.auth_service.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public static String user(String email) {
        return "otp:user:" + email;
    }

    /**Maximum otp expiration timing is 6 minutes later changes into 3 minutes*/
    public void saveToRedis(String key, Object value) {
        if (value == null) {
            return;
        }

        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(6));
        log.debug("Saved to Redis cache key={} with TTL={}s", key, Duration.ofMinutes(6).getSeconds());
    }

    public void deleteFromRedis(String key) {
        redisTemplate.delete(key);
    }

    public <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }

        // If the object is already the right type, just return it
        if (type.isInstance(value)) {
            return type.cast(value);
        }

        // Safely convert LinkedHashMap → target object
        return objectMapper.convertValue(value, type);
    }


}
