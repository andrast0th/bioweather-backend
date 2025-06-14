package com.example.bioweatherbackend.mapper;

import com.example.bioweatherbackend.entity.NotificationSubscriptionEntity;
import com.example.bioweatherbackend.model.api.notifications.SubscriptionDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface NotificationSubscriptionMapper {

    @Mapping(target = "updatedViaSubscribe", ignore = true)
    NotificationSubscriptionEntity toEntity(SubscriptionDto dto);

}