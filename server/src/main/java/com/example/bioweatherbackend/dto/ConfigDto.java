package com.example.bioweatherbackend.dto;

import com.example.bioweatherbackend.validation.ValidCron;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ConfigDto {
    @NotBlank
    @ValidCron
    private String notificationJobCron;

    private int bwNotificationThresholdMinMinutes;
    private int bwNotificationThresholdMaxMinutes;

    private int crNotificationThresholdMinMinutes;
    private int crNotificationThresholdMaxMinutes;
    
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid time format, expected HH:mm")
    private String bwTodayNotificationHour;

    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid time format, expected HH:mm")
    private String bwTomorrowNotificationHour;
}