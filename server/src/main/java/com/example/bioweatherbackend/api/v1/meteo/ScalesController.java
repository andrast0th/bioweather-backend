package com.example.bioweatherbackend.api.v1.meteo;

import com.example.bioweatherbackend.dto.weather.ApiBioWeatherByDate;
import com.example.bioweatherbackend.service.meteo.MeteoNewsDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.bioweatherbackend.service.meteo.MeteoNewsDataService.DEFAULT_LANGUAGE;

@RestController
@RequestMapping("/api/v1/scales")
@AllArgsConstructor
public class ScalesController {

    private final MeteoNewsDataService meteoNewsDataService;

    @GetMapping("/{id}")
    public List<ApiBioWeatherByDate> getByLocationId(@PathVariable("id") String id, @RequestHeader(value = "Accept-Language", defaultValue = DEFAULT_LANGUAGE) String language) {
        return meteoNewsDataService.getScalesByLocationId(id, language);
    }

}
