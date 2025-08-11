package com.example.bioweatherbackend.mapper;

import com.example.bioweatherbackend.dto.notifications.DeviceDto;
import com.example.bioweatherbackend.dto.notifications.PushTicketDto;
import com.example.bioweatherbackend.dto.notifications.SubscriptionDto;
import com.example.bioweatherbackend.entity.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DashboardMapper {

    @Mapping(source = "device.userId", target = "userId")
    @Mapping(source = "device.deviceInfo", target = "deviceInfo")
    @Mapping(source = "device.language", target = "language")
    @Mapping(source = "device.timezoneOffset", target = "timezoneOffset")
    SubscriptionDto toSubscriptionDto(NotificationSubscriptionEntity entity);

    @Mapping(source = "deviceInfo", target = "deviceInfo", qualifiedByName = "customDeviceInfo")
    DeviceDto toDeviceDto(DeviceEntity entity);
    @Named("customDeviceInfo")
    default Map<String, String> customDeviceInfo(String deviceInfo) {

        Map<String, String> result = new HashMap<>();
        var values = deviceInfo.split("\\|");

        Arrays.stream(values).forEach(entry -> {
            var key = entry.split(":")[0];
            var value = entry.split(":")[1];
            result.put(key, value);
        });

        return result;
    }


    List<DeviceDto> toDeviceDtoList(List<DeviceEntity> entities);

    @Mapping(source = "device.pushToken", target = "pushToken")
    PushTicketDto toPushTicketDto(PushTicketEntity entity);
    List<PushTicketDto> toPushTicketDtoList(List<PushTicketEntity> entity);

    TranslationDto toTranslationDto(TranslationEntity entity);
    TranslationEntity toTranslationEntity(TranslationDto entity);
    List<TranslationDto> toTranslationDtoList(List<TranslationEntity> entity);
}
