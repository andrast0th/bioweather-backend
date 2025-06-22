package com.example.bioweatherbackend.model.weather;

import lombok.Data;

@Data
public class ApiBioWeatherCondition {
    private String condition;
    private int severity;
}