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
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WeatherService {

    private static final String BASE_URL = "https://api.openweathermap.org/data/3.0/onecall";
    private static final String API_KEY = System.getenv("OPENWEATHERMAP_API_KEY");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final ConcurrentHashMap<String, WeatherForecastDto> cache = new ConcurrentHashMap<>();

    public WeatherForecastDto fetchWeather(double lat, double lng) {

        // Check cache first
        String cacheKey = "" + lat + lng;
        WeatherForecastDto cachedEntry = cache.get(cacheKey);

        if (cachedEntry != null && !isCacheExpired(cachedEntry.getCurrent().getDt(), cachedEntry.getTimezoneOffset())) {
            log.info("Cache hit: Returning cached response. Cache size: {}", cache.size());
            return cachedEntry;
        }

        String url = BASE_URL + "?lat=" + lat + "&lon=" + lng + "&appid=" + API_KEY;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseBody = EntityUtils.toString(response.getEntity());

                WeatherForecastDto dto = objectMapper.readValue(responseBody, WeatherForecastDto.class);
                cache.put(cacheKey, dto); // Store response in cache

                return dto;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int getCacheSize() {
        return cache.size();
    }

    public boolean isCacheExpired(long millis, Integer timezoneOffset) {
        // get the time now for the fixed offset
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.ofTotalSeconds(timezoneOffset));

        // get a zoned date time from millis with a given offset
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(millis), ZoneOffset.ofTotalSeconds(timezoneOffset));

        // is different day
        return now.getDayOfYear() != zdt.getDayOfYear();
    }

}
