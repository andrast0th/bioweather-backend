package com.example.bioweatherbackend.model.weather;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CumulationPeriod {
    H3("3h"),
    H6("6h"),
    H12("12h"),
    H24("24h"),
    D1("1d");

    private final String value;

    CumulationPeriod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @JsonCreator
    public static CumulationPeriod fromValue(String value) {
        for (CumulationPeriod period : values()) {
            if (period.value.equalsIgnoreCase(value)) {
                return period;
            }
        }
        throw new IllegalArgumentException("Unknown period: " + value);
    }
}