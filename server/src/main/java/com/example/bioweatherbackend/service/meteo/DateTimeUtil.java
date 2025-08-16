package com.example.bioweatherbackend.service.meteo;

import com.example.bioweatherbackend.dto.weather.ApiLocation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class DateTimeUtil {

    public ZonedDateTime getDateTimeForLocation(ApiLocation location) {
        // Implement logic to determine the date for the location
        // This could be based on the current date, or some other logic
        int offset = location.getUtcOffset();
        var offsetUnit = location.getUtcOffsetUnit();

        return switch (offsetUnit) {
            case "h" -> ZonedDateTime.now(ZoneOffset.ofHours(offset));
            case "m" -> ZonedDateTime.now(ZoneOffset.ofTotalSeconds(offset * 60));
            case "s" -> ZonedDateTime.now(ZoneOffset.ofTotalSeconds(offset));
            default -> throw new IllegalArgumentException("Unsupported UTC offset unit: " + offsetUnit);
        };
    }

    public LocalDateTime roundToNextQuarterHour(LocalDateTime dateTime) {
        int minutes = dateTime.getMinute();
        int remainder = minutes % 15;
        int minutesToAdd = remainder == 0 ? 0 : 15 - remainder;
        return dateTime.plusMinutes(minutesToAdd).withSecond(0).withNano(0);
    }

}
