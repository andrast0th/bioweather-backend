package com.example.bioweatherbackend.api.v1.meteo;

import com.example.bioweatherbackend.dto.weather.ApiLocation;
import com.example.bioweatherbackend.dto.weather.ApiSearchLocation;
import com.example.bioweatherbackend.service.meteo.MeteoNewsDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.bioweatherbackend.service.meteo.MeteoNewsDataService.DEFAULT_LANGUAGE;

@RestController
@RequestMapping("/api/v1/locations")
@AllArgsConstructor
public class LocationsController {

    private final MeteoNewsDataService meteoNewsDataService;

    @GetMapping("/{id}")
    public ApiLocation getById(@PathVariable("id") String id,
                               @RequestHeader(value = "Accept-Language", defaultValue = DEFAULT_LANGUAGE) String language) {
        return meteoNewsDataService.getLocationById(id, language);
    }

    @GetMapping("/georef/lat/{lat}/lon/{lon}")
    public ApiLocation geoRef(@PathVariable("lat") String lat,
                              @PathVariable("lon") String lon,
                              @RequestHeader(value = "Accept-Language", defaultValue = DEFAULT_LANGUAGE) String language) {
        return meteoNewsDataService.geoRef(lat, lon, language);
    }

    @GetMapping("/search/{searchQuery}")
    public List<ApiSearchLocation> searchLocations(@PathVariable("searchQuery") String searchQuery,
                                                   @RequestHeader(value = "Accept-Language", defaultValue = DEFAULT_LANGUAGE) String language) {

        return meteoNewsDataService.searchLocations(searchQuery, language);
    }

}