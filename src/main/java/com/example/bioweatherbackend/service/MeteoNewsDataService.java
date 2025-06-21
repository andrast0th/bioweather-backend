package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.mapper.AstronomyMapper;
import com.example.bioweatherbackend.mapper.LocationMapper;
import com.example.bioweatherbackend.mapper.WeatherMapper;
import com.example.bioweatherbackend.model.weather.*;
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
    private final AstronomyMapper astronomyMapper;
    private final String LANG = "en";

    public MeteoNewsDataService(@Qualifier("meteoNewsRestClient") RestClient restClient, LocationMapper locationMapper, WeatherMapper weatherMapper, AstronomyMapper astronomyMapper) {
        this.restClient = restClient;
        this.locationMapper = locationMapper;
        this.weatherMapper = weatherMapper;
        this.astronomyMapper = astronomyMapper;
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

    @Cacheable("astronomyByLocationId")
    public List<ApiAstronomy> getAstronomyByLocationId(String id, String beginDate, String endDate) {
        Astronomy response = restClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("astronomy/id/{id}.xml")
                            .queryParam("lang", LANG);

                    if (StringUtils.isNotEmpty(beginDate)) {
                        builder = builder.queryParam("begin", beginDate);
                    }
                    if (StringUtils.isNotEmpty(endDate)) {
                        builder = builder.queryParam("end", endDate);
                    }

                    return builder.build(id.trim());
                })
                .retrieve()
                .body(Astronomy.class);

        var apiResponse = astronomyMapper.toApiResponse(response);
        var location = getLocationById(id);
        apiResponse.forEach(astronomy -> {
            astronomy.setUtcOffset(location.getUtcOffset());
            astronomy.setUtcOffsetUnit(location.getUtcOffsetUnit());
        });

        return apiResponse;
    }

}
