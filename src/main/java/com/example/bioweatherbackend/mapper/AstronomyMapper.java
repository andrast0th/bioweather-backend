package com.example.bioweatherbackend.mapper;

import com.example.bioweatherbackend.dto.weather.ApiAstronomy;
import net.meteonews.feeds.schema.Astronomy;
import net.meteonews.feeds.schema.AstronomyDay;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AstronomyMapper {

    default ApiAstronomy toDto(AstronomyDay astronomyDay) {
        ApiAstronomy astronomy = new ApiAstronomy();

        astronomy.setDate(astronomyDay.getDate());

        astronomyDay.getContent().forEach(content -> {
            switch (content.getName().getLocalPart()) {
                case "sunrise":
                    astronomy.setSunrise(content.getValue());
                    break;
                case "sunset":
                    astronomy.setSunset(content.getValue());
                    break;
                case "moonrise":
                    astronomy.setMoonrise(content.getValue());
                    break;
                case "moonset":
                    astronomy.setMoonset(content.getValue());
                    break;
            }
        });

        return astronomy;
    }

    default List<ApiAstronomy> toApiResponse(Astronomy astronomy) {
        return astronomy
                .getContent()
                .getDay()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
