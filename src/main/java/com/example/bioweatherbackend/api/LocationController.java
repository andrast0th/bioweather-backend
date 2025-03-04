package com.example.bioweatherbackend.api;

import com.example.bioweatherbackend.model.NearbyPlace;
import com.example.bioweatherbackend.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("location")
@AllArgsConstructor
public class LocationController {

    private LocationService locationService;

    @GetMapping(value = "search", produces = {"application/json"})
    public @ResponseBody String search(@RequestParam(name = "q") String query) {
        return locationService.search(query);
    }

    @GetMapping(value = "georeference", produces = {"application/json"})
    public @ResponseBody NearbyPlace geoReference(@RequestParam(defaultValue = "0") Double lat, @RequestParam(defaultValue = "0") Double lon) {
        List<NearbyPlace> nearbyPlaces = locationService.fetchNearbyPlace(lat, lon);

        if (nearbyPlaces.isEmpty()) {
            throw new RuntimeException("Place not found.");
        }

        return nearbyPlaces.getFirst();
    }

}