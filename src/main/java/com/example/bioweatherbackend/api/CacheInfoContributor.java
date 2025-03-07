package com.example.bioweatherbackend.api;

import com.example.bioweatherbackend.service.LocationService;
import com.example.bioweatherbackend.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class CacheInfoContributor implements InfoContributor {

    private LocationService locationService;
    private WeatherService weatherService;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Integer> cacheDetails = new HashMap<>();
        cacheDetails.put("location", locationService.getCacheSize());
        cacheDetails.put("weather", weatherService.getCacheSize());

        builder.withDetail("cache-size", cacheDetails);
    }
}