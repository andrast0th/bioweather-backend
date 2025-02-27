package com.example.bioweatherbackend.model;

public record BioWeatherConditionDto(
        String condition,
        int severity
){}
