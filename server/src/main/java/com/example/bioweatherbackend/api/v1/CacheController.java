package com.example.bioweatherbackend.api.v1;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/cache")
@Slf4j
@AllArgsConstructor
public class CacheController {

    private final CacheManager cacheManager;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, CacheStats> getCacheInfo() {
        Map<String, CacheStats> response = new HashMap<>();

        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache<Object, Object> cache = (Cache<Object, Object>) cacheManager.getCache(cacheName).getNativeCache();
            response.put(cacheName, cache.stats());
        });

        return response;

    }

}
