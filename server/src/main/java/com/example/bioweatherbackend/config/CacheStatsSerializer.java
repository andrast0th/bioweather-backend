package com.example.bioweatherbackend.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.benmanes.caffeine.cache.stats.CacheStats;

import java.io.IOException;

public class CacheStatsSerializer extends JsonSerializer<CacheStats> {
    @Override
    public void serialize(CacheStats stats, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("requestCount", stats.requestCount());
        gen.writeNumberField("hitCount", stats.hitCount());
        gen.writeNumberField("missCount", stats.missCount());
        gen.writeNumberField("loadSuccessCount", stats.loadSuccessCount());
        gen.writeNumberField("loadFailureCount", stats.loadFailureCount());
        gen.writeNumberField("totalLoadTime", stats.totalLoadTime());
        gen.writeNumberField("evictionCount", stats.evictionCount());
        gen.writeNumberField("evictionWeight", stats.evictionWeight());
        gen.writeEndObject();
    }
}