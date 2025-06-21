package com.example.bioweatherbackend.model.weather;

import lombok.Data;

@Data
public class ApiAstronomy {
    private String date; // YYYY-MM-DD
    private int utcOffset;
    private String utcOffsetUnit;
    private String sunrise;
    private String sunset;
    private String moonrise;
    private String moonset;
}