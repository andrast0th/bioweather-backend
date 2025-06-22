package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.model.weather.ApiBioWeatherByDate;
import com.example.bioweatherbackend.service.MeteoNewsDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scales")
@AllArgsConstructor
public class ScalesController {

    private final MeteoNewsDataService meteoNewsDataService;

    @GetMapping("/{id}")
    public List<ApiBioWeatherByDate> getByLocationId(@PathVariable("id") String id) {
        return meteoNewsDataService.getScalesByLocationId(id);
    }

}
