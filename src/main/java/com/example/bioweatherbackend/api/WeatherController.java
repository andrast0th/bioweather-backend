package com.example.bioweatherbackend.api;

import com.example.bioweatherbackend.model.BioWeatherConditionDto;
import com.example.bioweatherbackend.model.BioWeatherForecastDto;
import com.example.bioweatherbackend.model.WeatherResponseDto;
import com.example.bioweatherbackend.model.weather.WeatherForecastDto;
import com.example.bioweatherbackend.service.LocationService;
import com.example.bioweatherbackend.service.WeatherService;
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

@Controller
@RequestMapping("weather")
@AllArgsConstructor
public class WeatherController {

    private WeatherService weatherService;

    @GetMapping
    public @ResponseBody WeatherResponseDto getWeather(@RequestParam(defaultValue = "0") Double lat, @RequestParam(defaultValue = "0") Double lng) {
        WeatherResponseDto weatherResponseDto = new WeatherResponseDto();

        // weather
        WeatherForecastDto weatherForecastDto = weatherService.fetchWeather(lat, lng);
        weatherResponseDto.setForecast(weatherForecastDto);

        // Bio
        LocalDate today = LocalDate.now();
        weatherResponseDto.getBioWeather().add(getMockedBioWeather(today));
        weatherResponseDto.getBioWeather().add(getMockedBioWeather(today.plusDays(1)));

        weatherResponseDto.setDt(ZonedDateTime.now(ZoneId.of("UTC")));
        return weatherResponseDto;

    }

    private BioWeatherForecastDto getMockedBioWeather(LocalDate date) {
        var faker = new Faker();

        BioWeatherForecastDto forecastDto = new BioWeatherForecastDto();

        forecastDto.setDate(date);
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

        return forecastDto;
    }

}