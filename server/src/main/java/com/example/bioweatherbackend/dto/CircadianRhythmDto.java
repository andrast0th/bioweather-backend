package com.example.bioweatherbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CircadianRhythmDto {
    private String dateStr; // YYYY-MM-DD
    private int utcOffset;
    private String utcOffsetUnit;
    private LocalDateTime sunrise;
    private LocalDateTime sunset;
    private LocalDateTime bed;
    private LocalDateTime wakeUp;
    private LocalDateTime lastMeal;
    private LocalDateTime peakAlertnessStart;
    private LocalDateTime peakAlertnessEnd;
    private LocalDateTime nextRestPeriodStart;
    private LocalDateTime nextRestPeriodEnd;
    private LocalDateTime exerciseStart;
    private LocalDateTime exerciseEnd;
}