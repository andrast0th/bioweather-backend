package com.example.bioweatherbackend.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiSearchLocation {
    private String id;
    private String countryCode;
    private String name;
    private String subdivision;
}