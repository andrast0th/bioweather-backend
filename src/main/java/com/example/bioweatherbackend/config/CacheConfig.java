package com.example.bioweatherbackend.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCache weatherCache = new CaffeineCache("weatherByLocationId",
            Caffeine.newBuilder()
                .expireAfter(new ExpireAtNextUtcMidnight())
                .build());

        CaffeineCache searchLocations = new CaffeineCache("searchLocations",
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(java.time.Duration.ofMinutes(10))
                        .build());

        CaffeineCache locationById = new CaffeineCache("locationById",
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(java.time.Duration.ofMinutes(10))
                        .build());

        CaffeineCache geoRefLocation = new CaffeineCache("geoRefLocation",
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(java.time.Duration.ofMinutes(10))
                        .build());

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(weatherCache, searchLocations, locationById, geoRefLocation));
        return manager;
    }
}