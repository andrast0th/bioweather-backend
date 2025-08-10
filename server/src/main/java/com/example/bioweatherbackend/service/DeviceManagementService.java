package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.dto.notifications.SubscriptionDto;
import com.example.bioweatherbackend.entity.DeviceEntity;
import com.example.bioweatherbackend.entity.NotificationSubscriptionEntity;
import com.example.bioweatherbackend.repository.DeviceRepository;
import com.example.bioweatherbackend.repository.NotificationSubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class DeviceManagementService {

    private final DeviceRepository deviceRepository;
    private final NotificationSubscriptionRepository subscriptionRepository;

    @Transactional
    public void subscribe(SubscriptionDto subscriptionDto) {
        deviceRepository.findById(subscriptionDto.getDeviceInfo())
                .ifPresentOrElse(deviceEntity -> {
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
        device.setDisabled(false);
    }

    private void saveNotificationTypes(SubscriptionDto subscriptionDto) {
        subscriptionRepository.deleteByPushTokenAndLocationId(subscriptionDto.getPushToken(), subscriptionDto.getLocationId());
        var subs = subscriptionDto.getNotificationTypes()
                .stream()
                .map(type -> {
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

}
