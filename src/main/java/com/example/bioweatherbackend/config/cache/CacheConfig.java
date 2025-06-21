package com.example.bioweatherbackend.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCache weatherCache = buildUtcAfterMidnightCache("weatherByLocationId");
        CaffeineCache astronomyCache = buildUtcAfterMidnightCache("astronomyByLocationId");
        CaffeineCache searchLocations = build10MinuteCache("searchLocations");
        CaffeineCache locationById = build10MinuteCache("locationById");
        CaffeineCache geoRefLocation = build10MinuteCache("geoRefLocation");

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(weatherCache, searchLocations, locationById, geoRefLocation, astronomyCache));
        return manager;
    }

    private CaffeineCache build10MinuteCache(String name) {
        return new CaffeineCache(name,
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .build());
    }

    private CaffeineCache buildUtcAfterMidnightCache(String name) {
        return new CaffeineCache(name,
                Caffeine.newBuilder()
                        .expireAfter(new ExpireAtNextUtcMidnight())
                        .build());
    }
}