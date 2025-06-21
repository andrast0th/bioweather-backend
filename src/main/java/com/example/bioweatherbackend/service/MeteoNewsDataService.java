package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.mapper.LocationMapper;
import com.example.bioweatherbackend.mapper.WeatherMapper;
import com.example.bioweatherbackend.model.weather.ApiLocation;
import com.example.bioweatherbackend.model.weather.ApiSearchLocation;
import com.example.bioweatherbackend.model.weather.ApiWeatherForecast;
import com.example.bioweatherbackend.model.weather.CumulationPeriod;
import io.micrometer.common.util.StringUtils;
import net.meteonews.feeds.schema.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class MeteoNewsDataService {

    private final RestClient restClient;
    private final LocationMapper locationMapper;
    private final WeatherMapper weatherMapper;
    private final String LANG = "en";

    public MeteoNewsDataService(@Qualifier("meteoNewsRestClient") RestClient restClient, LocationMapper locationMapper, WeatherMapper weatherMapper) {
        this.restClient = restClient;
        this.locationMapper = locationMapper;
        this.weatherMapper = weatherMapper;
    }

    @Cacheable("locationById")
    public ApiLocation getLocationById(String id) {
        Geonames response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("geonames/id/{id}.xml")
                        .queryParam("lang", LANG)
                        .build(id.trim()))
                .retrieve()
                .body(Geonames.class);

        return locationMapper.toLocationDto(response);
    }

    @Cacheable("geoRefLocation")
    public ApiLocation geoRef(String lat, String lon) {
        Geo response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geo/id.xml")
                        .queryParam("lang", LANG)
                        .queryParam("lat", lat.trim())
                        .queryParam("lon", lon.trim())
                        .build())
                .retrieve()
                .body(Geo.class);

        return Optional.ofNullable(response)
                .map(Geo::getContent)
                .map(GeoContent::getId)
                .map(this::getLocationById)
                .orElseThrow(() -> new IllegalArgumentException("No location found for lat: " + lat + ", lon: " + lon));
    }

    @Cacheable("searchLocations")
    public List<ApiSearchLocation> searchLocations(String searchQuery) {
        Search response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("search/{query}.xml")
                        .queryParam("autofill", 0)
                        .queryParam("limit", 10)
                        .queryParam("lang", LANG)
                        .build(searchQuery.trim()))
                .retrieve()
                .body(Search.class);

        List<ApiSearchLocation> res = locationMapper.toSearchDtoList(response);
        res = res.stream().filter(val -> StringUtils.isNotEmpty(val.getSubdivision())).toList();
        return res;
    }

    @Cacheable("weatherByLocationId")
    public List<ApiWeatherForecast> getWeatherByLocationId(String id, CumulationPeriod period) {
        Forecasts response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("forecasts/id/{id}.xml")
                        .queryParam("lang", LANG)
                        .queryParam("cumulation", period.getValue())
                        .build(id.trim()))
                .retrieve()
                .body(Forecasts.class);

        return weatherMapper.toWeatherForecastDto(response);
    }

}
