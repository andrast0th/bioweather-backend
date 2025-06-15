package com.example.bioweatherbackend.mapper;

import com.example.bioweatherbackend.entity.NotificationSubscriptionEntity;
import com.example.bioweatherbackend.model.api.notifications.NotificationSubscriptionDto;
import com.example.bioweatherbackend.model.api.notifications.SubscriptionDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface NotificationSubscriptionMapper {

    @Mapping(target = "updatedViaSubscribe", ignore = true)
    NotificationSubscriptionEntity toEntity(SubscriptionDto dto);

    NotificationSubscriptionDto toDto(NotificationSubscriptionEntity entity);

    default ZonedDateTime map(Instant value) {
        return value == null ? null : value.atZone(ZoneId.of("UTC"));
    }
}