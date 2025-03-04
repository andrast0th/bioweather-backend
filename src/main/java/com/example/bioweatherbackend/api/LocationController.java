package com.example.bioweatherbackend.api;

import com.example.bioweatherbackend.model.Place;
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
    public @ResponseBody List<Place>  search(@RequestParam(name = "q") String query) {
        return locationService.search(query);
    }

    @GetMapping(value = "georeference", produces = {"application/json"})
    public @ResponseBody Place geoReference(@RequestParam(defaultValue = "0") Double lat, @RequestParam(defaultValue = "0") Double lng) {
        List<Place> places = locationService.fetchNearbyPlace(lat, lng);

        if (places.isEmpty()) {
            throw new RuntimeException("Place not found.");
        }

        return places.getFirst();
    }

}