package com.example.bioweatherbackend.config;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class ExpireAtNextUtcMidnight implements Expiry<Object, Object> {

    @Override
    public long expireAfterCreate(Object key, Object value, long currentTime) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime nextMidnight = now.plusDays(1).toLocalDate().atStartOfDay(ZoneOffset.UTC);
        long seconds = Duration.between(now, nextMidnight).getSeconds();

        return TimeUnit.NANOSECONDS.convert(seconds, TimeUnit.SECONDS);
    }

    @Override
    public long expireAfterUpdate(Object o, Object o2, long currentTime, @NonNegative long currentDuration) {
        return -1;
    }

    @Override
    public long expireAfterRead(Object o, Object o2, long currentTime, @NonNegative long currentDuration) {
        return currentDuration;
    }

}