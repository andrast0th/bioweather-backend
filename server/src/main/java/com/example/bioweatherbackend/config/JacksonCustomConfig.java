package com.example.bioweatherbackend.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonCustomConfig {

    @Bean
    public SimpleModule cacheModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(CacheStats.class, new CacheStatsSerializer());
        return module;
    }

}