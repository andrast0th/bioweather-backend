package com.example.bioweatherbackend.api;

import com.example.bioweatherbackend.model.BioWeatherConditionDto;
import com.example.bioweatherbackend.model.BioWeatherForecastDto;
import com.example.bioweatherbackend.model.Place;
import com.example.bioweatherbackend.model.WeatherDto;
import com.example.bioweatherbackend.service.LocationService;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Controller
@RequestMapping("weather")
@AllArgsConstructor
public class WeatherController {

    private LocationService locationService;

    @GetMapping
    public @ResponseBody WeatherDto getWeather(@RequestParam(defaultValue = "0") Double lat, @RequestParam(defaultValue = "0") Double lng) {
        WeatherDto weatherDto = new WeatherDto();

        List<Place> places = locationService.fetchNearbyPlace(lat, lng);
        if (places.isEmpty()) {
            throw new RuntimeException("Place not found.");
        }

        Place place = places.getFirst();

        weatherDto.setLat(Double.parseDouble(place.getLat()));
        weatherDto.setLng(Double.parseDouble(place.getLng()));
        weatherDto.setCountryCode(place.getCountryCode());
        weatherDto.setCountryName(place.getCountryName());

        weatherDto.setDt(ZonedDateTime.now(ZoneId.of("UTC")));

        var faker = new Faker();

        weatherDto.setPlaceName(place.getName());
        weatherDto.setTemperature(faker.weather().temperatureCelsius());
        weatherDto.setWeatherDesc(faker.weather().description());

        BioWeatherForecastDto forecastDto = new BioWeatherForecastDto();
        forecastDto.setDate(LocalDate.now());
        forecastDto.getConditions().add(new BioWeatherConditionDto("cardiovascular system", faker.number().numberBetween(0, 4)));
        forecastDto.getConditions().add(new BioWeatherConditionDto("gastrointestinal tract", faker.number().numberBetween(0, 4)));
        forecastDto.getConditions().add(new BioWeatherConditionDto("headache/migraine", faker.number().numberBetween(0, 4)));
        forecastDto.getConditions().add(new BioWeatherConditionDto("high blood pressure", faker.number().numberBetween(0, 4)));
        forecastDto.getConditions().add(new BioWeatherConditionDto("low blood pressure", faker.number().numberBetween(0, 4)));
        forecastDto.getConditions().add(new BioWeatherConditionDto("nervous system", faker.number().numberBetween(0, 4)));
        forecastDto.getConditions().add(new BioWeatherConditionDto("respiratory tract", faker.number().numberBetween(0, 4)));
        forecastDto.getConditions().add(new BioWeatherConditionDto("rheumatism", faker.number().numberBetween(0, 4)));
        forecastDto.getConditions().add(new BioWeatherConditionDto("skin", faker.number().numberBetween(0, 4)));
        forecastDto.getConditions().add(new BioWeatherConditionDto("sleep quality", faker.number().numberBetween(0, 4)));

        weatherDto.getBioWeather().add(forecastDto);

        return weatherDto;

    }

}