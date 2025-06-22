package com.example.bioweatherbackend.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Helper class for snowLine/freezLevel (value/unit/ground)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiSnowLine {
    private String value;
    private String unit;
    private String ground;
}
