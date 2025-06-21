package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.mapper.MeteoNewsApiMapper;
import com.example.bioweatherbackend.model.meteonews.ApiLocation;
import com.example.bioweatherbackend.model.meteonews.ApiSearchLocation;
import io.micrometer.common.util.StringUtils;
import net.meteonews.feeds.schema.Geo;
import net.meteonews.feeds.schema.GeoContent;
import net.meteonews.feeds.schema.Geonames;
import net.meteonews.feeds.schema.Search;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class MeteoNewsDataService {

    private final RestClient restClient;
    private final MeteoNewsApiMapper mapper;
    private final String LANG = "en";

    public MeteoNewsDataService(@Qualifier("meteoNewsRestClient") RestClient restClient, MeteoNewsApiMapper mapper) {
        this.restClient = restClient;
        this.mapper = mapper;
    }

    @Cacheable("locationById")
    public ApiLocation getLocationById(String id) {
        String url = "geonames/id/" + id.trim() + ".xml?lang=" + LANG;

        Geonames response = restClient.get()
                .uri(url)
                .retrieve()
                .body(Geonames.class);

        return mapper.toLocationDto(response);
    }

    @Cacheable("geoRefLocation")
    public ApiLocation geoRef(String lat, String lon) {
        String url = "/geo/id.xml?lang=" + LANG + "&lat=" + lat.trim() + "&lon=" + lon.trim();

        Geo response = restClient.get()
                .uri(url)
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
        String url = "search/" + searchQuery.trim() + ".xml?autofill=0&limit=10&lang=" + LANG;

        Search response = restClient.get()
                .uri(url)
                .retrieve()
                .body(Search.class);

        List<ApiSearchLocation> res = mapper.toSearchDtoList(response);
        res = res.stream().filter(val -> StringUtils.isNotEmpty(val.getSubdivision())).toList();
        return res;
    }

}
