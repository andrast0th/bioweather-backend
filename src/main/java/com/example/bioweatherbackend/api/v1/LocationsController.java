package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.model.meteonews.ApiLocation;
import com.example.bioweatherbackend.model.meteonews.ApiSearchLocation;
import com.example.bioweatherbackend.service.MeteoNewsDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@AllArgsConstructor
public class LocationsController {

    private final MeteoNewsDataService meteoNewsDataService;

    @GetMapping("/{id}")
    public ApiLocation getById(@PathVariable("id") String id) {
        return meteoNewsDataService.getLocationById(id);
    }

    @GetMapping("/georef/lat/{lat}/lon/{lon}")
    public ApiLocation geoRef(@PathVariable("lat") String lat, @PathVariable("lon") String lon) {
        return meteoNewsDataService.geoRef(lat, lon);
    }

    @GetMapping("/search/{searchQuery}")
    public List<ApiSearchLocation> searchLocations(@PathVariable("searchQuery") String searchQuery) {
        return meteoNewsDataService.searchLocations(searchQuery);
    }

}