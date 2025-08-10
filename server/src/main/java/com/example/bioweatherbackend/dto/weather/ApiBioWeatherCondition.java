package com.example.bioweatherbackend.dto.weather;

import lombok.Data;

@Data
public class ApiBioWeatherCondition {
    private String condition;
    private int severity;
}