package com.example.bioweatherbackend.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BioWeatherForecastDto {
    private LocalDate date;
    private List<BioWeatherConditionDto> conditions = new ArrayList<>();
}
