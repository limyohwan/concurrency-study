package com.example.stock.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisLockRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(Long id) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(String.valueOf(id), "lock", Duration.ofMillis(3000));
    }

    public Boolean unlock(Long id) {
        return redisTemplate.delete(String.valueOf(id));
    }
}
