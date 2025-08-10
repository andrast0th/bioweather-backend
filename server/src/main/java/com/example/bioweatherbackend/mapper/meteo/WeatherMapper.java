package com.example.bioweatherbackend.mapper.meteo;

import com.example.bioweatherbackend.dto.weather.ApiWeatherForecast;
import net.meteonews.feeds.schema.Forecasts;
import net.meteonews.feeds.schema.ForecastsTimeperiod;
import org.mapstruct.Condition;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WeatherMapper {

    @Mapping(target = "lastUpdate", expression = "java(com.example.bioweatherbackend.mapper.DateTimeUtils.convertXmlDateTimeFormatToIso(forecastsTimeperiod.getLastUpdate()))")
    @Mapping(target = "endDatetime", expression = "java(com.example.bioweatherbackend.mapper.DateTimeUtils.convertXmlDateTimeFormatToIso(forecastsTimeperiod.getEndDatetime()))")
    ApiWeatherForecast toWeatherForecastDto(ForecastsTimeperiod forecastsTimeperiod);

    default List<ApiWeatherForecast> toWeatherForecastDto(Forecasts forecasts) {
        return forecasts.getContent().getTimeperiod().stream()
                .map(this::toWeatherForecastDto)
                .collect(Collectors.toList());
    }

    @Condition
    default boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }
}
