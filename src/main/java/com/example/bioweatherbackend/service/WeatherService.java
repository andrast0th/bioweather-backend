package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.model.weather.WeatherForecastDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

class CacheEntry {
    WeatherForecastDto response;
    long timestamp;

    CacheEntry(WeatherForecastDto response, long timestamp) {
        this.response = response;
        this.timestamp = timestamp;
    }
}

@Service
@Slf4j
public class WeatherService {

    private static final String BASE_URL = "https://api.openweathermap.org/data/3.0/onecall";
    private static final String API_KEY = System.getenv("OPENWEATHERMAP_API_KEY");
    private static final long CACHE_EXPIRY_TIME = 24 * 60 * 60 * 1000; // 26h

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public WeatherForecastDto fetchWeather(double lat, double lng) {

        long currentTime = System.currentTimeMillis();

        // Check cache first
        String cacheKey = "" + lat + lng;
        CacheEntry cachedEntry = cache.get(cacheKey);

        if (cachedEntry != null && (currentTime - cachedEntry.timestamp) < CACHE_EXPIRY_TIME) {
            log.info("Cache hit: Returning cached response. Cache size: {}", cache.size());
            return cachedEntry.response;
        }

        String url = BASE_URL + "?lat=" + lat + "&lon=" + lng + "&appid=" + API_KEY;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseBody = EntityUtils.toString(response.getEntity());

                WeatherForecastDto dto = objectMapper.readValue(responseBody, WeatherForecastDto.class);
                cache.put(cacheKey, new CacheEntry(dto, currentTime)); // Store response in cache

                return dto;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int getCacheSize() {
        return cache.size();
    }

}
