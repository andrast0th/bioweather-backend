package com.example.bioweatherbackend.dto.weather;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CumulationPeriodConverter implements Converter<String, CumulationPeriod> {
    @Override
    public CumulationPeriod convert(String source) {
        return CumulationPeriod.fromValue(source);
    }
}