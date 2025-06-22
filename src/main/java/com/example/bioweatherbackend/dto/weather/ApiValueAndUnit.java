package com.example.bioweatherbackend.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiValueAndUnit<T> {
    private T value;
    private String unit;
}