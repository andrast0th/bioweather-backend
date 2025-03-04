package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.model.Place;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class LocationService {

    private static final String BASE_URL_NEARBY = "http://api.geonames.org/findNearbyPlaceNameJSON";
    private static final String BASE_URL_SEARCH = "http://api.geonames.org/searchJSON";
    private static final String USERNAME = "andris91";
    private static final long CACHE_EXPIRY_TIME = 10 * 60 * 1000; // 10 minutes

    private static final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private static class CacheEntry {
        String response;
        long timestamp;

        CacheEntry(String response, long timestamp) {
            this.response = response;
            this.timestamp = timestamp;
        }
    }

    public List<Place> fetchNearbyPlace(double lat, double lng) {
        String url = BASE_URL_NEARBY + "?lat=" + lat + "&lng=" + lng + "&username=" + USERNAME;
        String response;
        try {
            response = fetchFromApi(url);
            return parsePlaces(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Place> parsePlaces(String jsonResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        List<Place> places = new ArrayList<>();

        if (rootNode.has("geonames")) {
            for (JsonNode node : rootNode.get("geonames")) {
                Place place = objectMapper.treeToValue(node, Place.class);
                places.add(place);
            }
        }
        return places;
    }

    public List<Place> search(String query) {
        String url = BASE_URL_SEARCH + "?q=" + query + "&maxRows=10&lang=en&username=" + USERNAME + "&style=MEDIUM&featureClass=P";
        try {
            String response = fetchFromApi(url);
            return parsePlaces(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String fetchFromApi(String url) throws IOException {
        long currentTime = System.currentTimeMillis();

        // Check cache first
        CacheEntry cachedEntry = cache.get(url);
        if (cachedEntry != null && (currentTime - cachedEntry.timestamp) < CACHE_EXPIRY_TIME) {
            log.info("Cache hit: Returning cached response. Cache size: {}",cache.size());
            return cachedEntry.response;
        }

        // Make HTTP request
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                cache.put(url, new CacheEntry(responseBody, currentTime)); // Store response in cache
                return responseBody;
            }
        }
    }
}
