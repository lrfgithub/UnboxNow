package com.unboxnow.common.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class IdGenerator {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public IdGenerator(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getId(String counterName) {
        RedisAtomicInteger counter = new RedisAtomicInteger(
                counterName,
                Objects.requireNonNull(redisTemplate.getConnectionFactory())
        );
        int num = counter.incrementAndGet();
        counter.expire(15, TimeUnit.MINUTES);
        return getTimeStamp() + String.format("%0" + 3 + "d", num);
    }

    private String getTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
