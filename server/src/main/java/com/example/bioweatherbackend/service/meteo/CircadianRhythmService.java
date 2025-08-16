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
    private final DateTimeUtil dateTimeUtil;

    @Cacheable(CacheConfig.CIRCADIAN_RHYTHM)
    public CircadianRhythmDto getCircadianRhythm(String locationId, LocalDate localDate) {
        var location  = meteoNewsDataService.getLocationById(locationId);

        if (localDate == null) {
            var datetime = dateTimeUtil.getDateTimeForLocation(location);
            localDate = datetime.toLocalDate();
        }

        List<ApiAstronomy> astronomyList = meteoNewsDataService.getAstronomy(locationId, localDate.toString(), localDate.toString());
        ApiAstronomy astronomy = astronomyList.isEmpty() ? null : astronomyList.getFirst();

        CircadianRhythmDto result = new CircadianRhythmDto();
        result.setDateStr(localDate.toString());

        LocalDateTime sunrise = parseAstronomyTime(localDate, astronomy != null ? astronomy.getSunrise() : null);
        LocalDateTime sunset = parseAstronomyTime(localDate, astronomy != null ? astronomy.getSunset() : null);

        LocalDateTime sunriseMax = localDate.atTime(7, 0);

        LocalDateTime wakeUp;
        if (sunrise != null && sunset != null) {
            wakeUp = sunrise.isAfter(sunriseMax) ? dateTimeUtil.roundToNextQuarterHour(sunriseMax) : dateTimeUtil.roundToNextQuarterHour(sunrise);
            result.setSunrise(sunrise);
            result.setSunset(sunset);
        } else {
            wakeUp = dateTimeUtil.roundToNextQuarterHour(sunriseMax);
        }

        LocalDateTime bed = wakeUp.minusHours(8).withDayOfMonth(wakeUp.getDayOfMonth());
        result.setWakeUp(wakeUp);
        result.setBed(bed);

        result.setLastMeal(bed.minusHours(4));
        result.setPeakAlertnessStart(wakeUp.plusHours(3));
        result.setPeakAlertnessEnd(wakeUp.plusHours(5));
        result.setNextRestPeriodStart(wakeUp.plusHours(7));
        result.setNextRestPeriodEnd(wakeUp.plusHours(7).plusMinutes(30));
        result.setExerciseStart(wakeUp.plusHours(9));
        result.setExerciseEnd(wakeUp.plusHours(11));
        result.setUtcOffset(location.getUtcOffset());
        result.setUtcOffsetUnit(location.getUtcOffsetUnit());

        return result;
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

}