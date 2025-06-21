package com.example.bioweatherbackend.mapper;

import com.example.bioweatherbackend.model.weather.ApiLocation;
import com.example.bioweatherbackend.model.weather.ApiSearchLocation;
import net.meteonews.feeds.schema.Geonames;
import net.meteonews.feeds.schema.Search;
import net.meteonews.feeds.schema.Suggest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LocationMapper {

    // Map a single object
    @Mapping(source = "id", target = "id")
    @Mapping(source = "country", target = "countryCode")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "subdivision.name", target = "subdivision")
    ApiSearchLocation toSearchLocationDto(Suggest suggest);

    default List<ApiSearchLocation> toSearchDtoList(Search searchResponse) {
        return searchResponse.getContent().getSuggest().stream().map(this::toSearchLocationDto).collect(Collectors.toList());
    }

    @Mapping(source = "content.id", target = "id")
    @Mapping(source = "content.country", target = "countryCode")
    @Mapping(source = "content.name", target = "name")
    @Mapping(source = "content.subdivision.name", target = "subdivision")
    @Mapping(source = "content.lat", target = "lat")
    @Mapping(source = "content.lon", target = "lon")
    @Mapping(source = "content.utcOffset.value", target = "utcOffset")
    @Mapping(source = "content.utcOffset.unit", target = "utcOffsetUnit")
    ApiLocation toLocationDto(Geonames geonamesResponse);

}
