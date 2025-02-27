package com.example.bioweatherbackend.api;

import com.example.bioweatherbackend.model.BioWeatherConditionDto;
import com.example.bioweatherbackend.model.BioWeatherForecastDto;
import com.example.bioweatherbackend.model.WeatherDto;
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
public class WeatherController {

    @GetMapping
    public @ResponseBody WeatherDto getWeather(@RequestParam(defaultValue = "0") Double lat, @RequestParam(defaultValue = "0") Double lon) {
        WeatherDto weatherDto = new WeatherDto();

        weatherDto.setLat(lat);
        weatherDto.setLon(lon);
        weatherDto.setDt(ZonedDateTime.now(ZoneId.of("UTC")));

        var faker = new Faker();

        weatherDto.setPlaceName(faker.address().cityName());
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