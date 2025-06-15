package com.example.bioweatherbackend.mapper;

import com.example.bioweatherbackend.model.meteonews.ApiSearchLocation;
import net.meteonews.feeds.schema.Search;
import net.meteonews.feeds.schema.Suggest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MeteoNewsApiMapper {

    // Map a single object
    @Mapping(source = "id", target = "id")
    @Mapping(source = "country", target = "countryCode")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "subdivision.name", target = "subdivision")
    ApiSearchLocation toDto(Suggest suggest);

    default List<ApiSearchLocation> toDtoList(Search searchResponse) {
        return searchResponse.getContent().getSuggest().stream().map(this::toDto).collect(Collectors.toList());
    }
}
