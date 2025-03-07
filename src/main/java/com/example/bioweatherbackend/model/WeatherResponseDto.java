package com.example.bioweatherbackend.model;

import com.example.bioweatherbackend.model.weather.WeatherForecastDto;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WeatherResponseDto {

    private Place place;
    private WeatherForecastDto forecast;
    private List<BioWeatherForecastDto> bioWeather = new ArrayList<>();

    private ZonedDateTime dt;
}
