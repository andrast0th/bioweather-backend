package com.example.bioweatherbackend.api.v1.meteo;

import com.example.bioweatherbackend.dto.CircadianRhythmDto;
import com.example.bioweatherbackend.dto.weather.ApiAstronomy;
import com.example.bioweatherbackend.service.meteo.CircadianRhythmService;
import com.example.bioweatherbackend.service.meteo.MeteoNewsDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.example.bioweatherbackend.service.meteo.MeteoNewsDataService.DEFAULT_LANGUAGE;

@RestController
@RequestMapping("/api/v1/astronomy")
@AllArgsConstructor
public class AstronomyController {

    private final MeteoNewsDataService meteoNewsDataService;
    private final CircadianRhythmService circadianRhythmService;

    @GetMapping("/{id}")
    public List<ApiAstronomy> getByLocationId(@PathVariable("id") String id,
                                              @RequestParam(required = false) String beginDate,
                                              @RequestParam(required = false) String endDate,
                                              @RequestHeader(value = "Accept-Language", defaultValue = DEFAULT_LANGUAGE) String language) {
        return meteoNewsDataService.getAstronomy(id, beginDate, endDate, language);
    }

    @GetMapping("/{id}/circadian-rhythm")
    public CircadianRhythmDto getCircadianRhythm(@PathVariable("id") String locationId,
                                                 @RequestParam(required = false) LocalDate date,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = DEFAULT_LANGUAGE) String language) {
        return circadianRhythmService.getCircadianRhythm(locationId, date, language);
    }

}
