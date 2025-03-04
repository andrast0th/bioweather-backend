package com.example.bioweatherbackend.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WeatherDto {

    private double lat;
    private double lng;
    private String placeName;
    private String countryCode;
    private String countryName;

    private String  weatherDesc;
    private String  temperature;

    private List<BioWeatherForecastDto> bioWeather = new ArrayList<>();

    private ZonedDateTime dt;
}
