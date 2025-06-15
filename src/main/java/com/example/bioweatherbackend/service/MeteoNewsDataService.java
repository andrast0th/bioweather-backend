package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.mapper.MeteoNewsApiMapper;
import com.example.bioweatherbackend.model.meteonews.ApiSearchLocation;
import io.micrometer.common.util.StringUtils;
import net.meteonews.feeds.schema.Search;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class MeteoNewsDataService {

    private final RestClient restClient;
    private final MeteoNewsApiMapper mapper;
    private final String LANG = "en";

    public MeteoNewsDataService(@Qualifier("meteoNewsRestClient") RestClient restClient, MeteoNewsApiMapper mapper) {
        this.restClient = restClient;
        this.mapper = mapper;
    }

    @Cacheable("searchLocations")
    public List<ApiSearchLocation> searchLocations(String searchQuery) {
        String url = "search/"+searchQuery.trim()+".xml?autofill=0&limit=10&lang=" + LANG;

        Search search =  restClient.get()
                .uri(url)
                .retrieve()
                .body(Search.class);

        List<ApiSearchLocation> res = mapper.toDtoList(search);
        res = res.stream().filter(val -> StringUtils.isNotEmpty(val.getSubdivision())).toList();
        return res;
    }

}
