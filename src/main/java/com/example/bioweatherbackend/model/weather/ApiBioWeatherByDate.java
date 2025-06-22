package com.example.bioweatherbackend.model.weather;

import lombok.Data;

import java.util.List;

@Data
public class ApiBioWeatherByDate {
    private String date;
    private List<ApiBioWeatherCondition> conditions;
}