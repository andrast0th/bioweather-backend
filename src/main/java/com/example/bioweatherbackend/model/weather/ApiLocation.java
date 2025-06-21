package com.example.bioweatherbackend.model.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiLocation extends ApiSearchLocation {
    private String lat;
    private String lon;
    private int utcOffset;
    private String utcOffsetUnit;
}