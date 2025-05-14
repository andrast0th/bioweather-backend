package com.example.bioweatherbackend.mapper;

import com.example.bioweatherbackend.entity.NotificationSubscriptionEntity;
import com.example.bioweatherbackend.model.notifications.SubscriptionDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface NotificationSubscriptionMapper {

    @Mapping(target = "id.pushToken", source = "pushToken")
    @Mapping(target = "id.locationId", source = "locationId")
    NotificationSubscriptionEntity toEntity(SubscriptionDto dto);

    @Mapping(target = "pushToken", source = "id.pushToken")
    @Mapping(target = "locationId", source = "id.locationId")
    SubscriptionDto toDto(NotificationSubscriptionEntity entity);
}