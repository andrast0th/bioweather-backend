package com.example.bioweatherbackend.api;

import com.example.bioweatherbackend.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("location")
@AllArgsConstructor
public class LocationController {

    private LocationService locationService;

    @GetMapping(value = "search", produces = {"application/json"})
    public @ResponseBody String getWeather(@RequestParam(name = "q") String query) {
        return locationService.search(query);
    }

}