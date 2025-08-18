package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.dto.notifications.DeviceDto;
import com.example.bioweatherbackend.dto.notifications.SubscriptionDto;
import com.example.bioweatherbackend.entity.DeviceEntity;
import com.example.bioweatherbackend.entity.NotificationSubscriptionEntity;
import com.example.bioweatherbackend.mapper.DashboardMapper;
import com.example.bioweatherbackend.repository.DeviceRepository;
import com.example.bioweatherbackend.repository.NotificationSubscriptionRepository;
import com.example.bioweatherbackend.service.meteo.MeteoNewsDataService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class DeviceManagementService {

    private final DeviceRepository deviceRepository;
    private final NotificationSubscriptionRepository subscriptionRepository;
    private final DashboardMapper dashboardMapper;
    private final MeteoNewsDataService meteoNewsDataService;

    @Transactional
    public void subscribe(SubscriptionDto subscriptionDto) {
        deviceRepository.findById(subscriptionDto.getDeviceInfo()).ifPresentOrElse(deviceEntity -> {
            updateDeviceEntity(deviceEntity, subscriptionDto);
            deviceRepository.save(deviceEntity);
        }, () -> {
            var newEntity = new DeviceEntity();
            updateDeviceEntity(newEntity, subscriptionDto);
            deviceRepository.save(newEntity);
        });

        saveNotificationTypes(subscriptionDto);
    }

    private void updateDeviceEntity(DeviceEntity device, SubscriptionDto subscriptionDto) {
        device.setPushToken(subscriptionDto.getPushToken());
        device.setDeviceInfo(subscriptionDto.getDeviceInfo());
        device.setLanguage(subscriptionDto.getLanguage());
        device.setUserId(subscriptionDto.getUserId());
        device.setUpdatedTimestamp(Instant.now());
        device.setTimezoneOffset(subscriptionDto.getTimezoneOffset());
        device.setSelectedBwConditions(subscriptionDto.getSelectedBwConditions());
        device.setDisabled(false);
    }

    private void saveNotificationTypes(SubscriptionDto subscriptionDto) {
        subscriptionRepository.deleteByPushTokenAndLocationId(subscriptionDto.getPushToken(), subscriptionDto.getLocationId());
        var subs = subscriptionDto.getNotificationTypes().stream().map(type -> {
            var entity = new NotificationSubscriptionEntity();
            entity.setPushToken(subscriptionDto.getPushToken());
            entity.setLocationId(subscriptionDto.getLocationId());
            entity.setNotificationType(type);
            return entity;
        }).toList();

        subscriptionRepository.saveAll(subs);
    }

    @Transactional
    public void unsubscribe(SubscriptionDto subscriptionDto) {
        subscriptionRepository.deleteByPushTokenAndLocationId(subscriptionDto.getPushToken(), subscriptionDto.getLocationId());
    }

    @Transactional
    public List<SubscriptionDto> getSubscriptions(String pushToken) {
        var entities = subscriptionRepository.findByPushToken(pushToken);

        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        // Group by locationId
        var groupedByLoc = new ArrayList<SubscriptionDto>();
        var locationMap = new HashMap<String, SubscriptionDto>();

        for (var entity : entities) {
            var locationId = entity.getLocationId();
            var dto = locationMap.get(locationId);
            if (dto == null) {
                dto = dashboardMapper.toSubscriptionDto(entity);
                dto.getNotificationTypes().clear();
                dto.setLocationName(meteoNewsDataService.getLocationById(locationId).getName());
                locationMap.put(locationId, dto);
                groupedByLoc.add(dto);
            }
            dto.getNotificationTypes().add(entity.getNotificationType());
        }

        return groupedByLoc;
    }

    @Transactional
    public List<DeviceDto> getDevices(String query) {
        var entities = StringUtils.hasText(query)
                ? deviceRepository.findAllByDeviceInfoContainingIgnoreCaseOrderByUpdatedTimestampDesc(query)
                : deviceRepository.findAllByOrderByUpdatedTimestampDesc();

        return dashboardMapper.toDeviceDtoList(entities);
    }

}
