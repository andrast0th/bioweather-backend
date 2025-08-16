package com.example.bioweatherbackend.api.v1.meteo;

import com.example.bioweatherbackend.dto.weather.ApiAstronomy;
import com.example.bioweatherbackend.service.meteo.MeteoNewsDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/astronomy")
@AllArgsConstructor
public class AstronomyController {

    private final MeteoNewsDataService meteoNewsDataService;

    @GetMapping("/{id}")
    public List<ApiAstronomy> getByLocationId(@PathVariable("id") String id, @RequestParam(required = false) String beginDate, @RequestParam(required = false) String endDate) {
        return meteoNewsDataService.getAstronomy(id, beginDate, endDate);
    }

}
