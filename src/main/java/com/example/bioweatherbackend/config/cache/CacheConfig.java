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
    public static final String WEATHER = "weather";
    public static final String ASTRONOMY = "astronomy";
    public static final String SEARCH_LOCATIONS = "searchLocations";
    public static final String LOCATION = "location";
    public static final String GEO_REF_LOCATION = "geoRefLocation";
    public static final String SCALES = "scales";

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(
                buildUtcAfterMidnightCache(SCALES),
                buildUtcAfterMidnightCache(WEATHER),
                buildUtcAfterMidnightCache(ASTRONOMY),
                build10MinuteCache(SEARCH_LOCATIONS),
                build10MinuteCache(LOCATION),
                build10MinuteCache(GEO_REF_LOCATION)
        ));
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