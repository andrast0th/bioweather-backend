package com.example.bioweatherbackend.mapper;

import com.example.bioweatherbackend.model.weather.ApiBioWeatherByDate;
import com.example.bioweatherbackend.model.weather.ApiBioWeatherCondition;
import net.meteonews.feeds.schema.Scale;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ScalesMapper {

    default List<ApiBioWeatherByDate> toResponse(List<Scale> scales) {
        Map<String, List<ApiBioWeatherCondition>> conditionsByDateMap = new HashMap<>();

        for (Scale scale : scales) {

            scale.getForecast().forEach(forecast -> {
                ApiBioWeatherCondition condition = new ApiBioWeatherCondition();
                condition.setCondition(scale.getScaleId());
                condition.setSeverity(forecast.getValue().intValue());
                conditionsByDateMap.putIfAbsent(forecast.getDate(), new ArrayList<>());
                conditionsByDateMap.get(forecast.getDate()).add(condition);
            });
        }

        return conditionsByDateMap
                .entrySet()
                .stream()
                .map((entry) -> {
                    ApiBioWeatherByDate apiBioWeatherByDate = new ApiBioWeatherByDate();
                    apiBioWeatherByDate.setDate(entry.getKey());
                    apiBioWeatherByDate.setConditions(entry.getValue());
                    return apiBioWeatherByDate;
                })
                .toList();
    }
}
