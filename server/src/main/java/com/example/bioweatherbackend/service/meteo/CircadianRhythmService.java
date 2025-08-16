package com.example.bioweatherbackend.service.meteo;

import com.example.bioweatherbackend.config.cache.CacheConfig;
import com.example.bioweatherbackend.dto.CircadianRhythmDto;
import com.example.bioweatherbackend.dto.weather.ApiAstronomy;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class CircadianRhythmService {

    private final MeteoNewsDataService meteoNewsDataService;
    private final LocationService locationService;

    @Cacheable(CacheConfig.CIRCADIAN_RHYTHM)
    public CircadianRhythmDto getCircadianRhythm(String locationId, LocalDate localDate) {

        if (localDate == null) {
            var location  = meteoNewsDataService.getLocationById(locationId);
            var datetime = locationService.getDateTimeForLocation(location);
            localDate = datetime.toLocalDate();
        }

        List<ApiAstronomy> astronomyList = meteoNewsDataService.getAstronomy(locationId, localDate.toString(), localDate.toString());
        ApiAstronomy astronomy = astronomyList.isEmpty() ? null : astronomyList.get(0);

        CircadianRhythmDto circadian = new CircadianRhythmDto();
        circadian.setDateStr(localDate.toString());

        LocalDateTime sunrise = parseAstronomyTime(localDate, astronomy != null ? astronomy.getSunrise() : null);
        LocalDateTime sunset = parseAstronomyTime(localDate, astronomy != null ? astronomy.getSunset() : null);

        LocalDateTime sunriseMax = localDate.atTime(7, 0);

        LocalDateTime wakeUp;
        if (sunrise != null && sunset != null) {
            wakeUp = sunrise.isAfter(sunriseMax) ? roundToNextQuarterHour(sunriseMax) : roundToNextQuarterHour(sunrise);
            circadian.setSunrise(sunrise);
            circadian.setSunset(sunset);
        } else {
            wakeUp = roundToNextQuarterHour(sunriseMax);
        }

        LocalDateTime bed = wakeUp.minusHours(8).withDayOfMonth(wakeUp.getDayOfMonth());
        circadian.setWakeUp(wakeUp);
        circadian.setBed(bed);

        circadian.setLastMeal(bed.minusHours(4));
        circadian.setPeakAlertnessStart(wakeUp.plusHours(3));
        circadian.setPeakAlertnessEnd(wakeUp.plusHours(5));
        circadian.setNextRestPeriodStart(wakeUp.plusHours(7));
        circadian.setNextRestPeriodEnd(wakeUp.plusHours(7).plusMinutes(30));
        circadian.setExerciseStart(wakeUp.plusHours(9));
        circadian.setExerciseEnd(wakeUp.plusHours(11));

        return circadian;
    }

    private LocalDateTime parseAstronomyTime(LocalDate date, String time) {
        if (time == null) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
            return LocalDateTime.parse(date + " " + time, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime roundToNextQuarterHour(LocalDateTime dateTime) {
        int minutes = dateTime.getMinute();
        int remainder = minutes % 15;
        int minutesToAdd = remainder == 0 ? 0 : 15 - remainder;
        return dateTime.plusMinutes(minutesToAdd).withSecond(0).withNano(0);
    }
}