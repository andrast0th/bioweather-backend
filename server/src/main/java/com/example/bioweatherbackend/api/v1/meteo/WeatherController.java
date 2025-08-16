package com.example.bioweatherbackend.api.v1.meteo;

import com.example.bioweatherbackend.dto.weather.ApiWeatherForecast;
import com.example.bioweatherbackend.dto.weather.CumulationPeriod;
import com.example.bioweatherbackend.service.meteo.MeteoNewsDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
@AllArgsConstructor
public class WeatherController {

    private final MeteoNewsDataService meteoNewsDataService;

    @GetMapping("/{id}")
    public List<ApiWeatherForecast> getByLocationId(@PathVariable("id") String id, @RequestParam(value = "cumulation", required = false, defaultValue = "24h") CumulationPeriod period) {
        return meteoNewsDataService.getWeatherByLocationId(id, period);
    }

}
