package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.dto.notifications.*;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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
        subscriptionRepository.deleteByPushToken(subscriptionDto.getPushToken());

        var subs = new ArrayList<NotificationSubscriptionEntity>();
        subscriptionDto.getNotificationInfo().forEach(notificationDto -> {
            notificationDto.getNotificationTypes().forEach(notificationType -> {
                var entity = new NotificationSubscriptionEntity();
                entity.setPushToken(subscriptionDto.getPushToken());
                entity.setLocationId(notificationDto.getLocationId());
                entity.setNotificationType(notificationType);
                subs.add(entity);
            });
        });

        subscriptionRepository.saveAll(subs);
    }

    @Transactional
    public void unsubscribe(UnsubscribeDto unsubscribeDto) {
        subscriptionRepository.deleteByPushTokenAndLocationId(unsubscribeDto.getPushToken(), unsubscribeDto.getLocationId());
    }

    @Transactional
    public SubscriptionDto getSubscriptions(String pushToken) {
        var device = deviceRepository.findById(pushToken).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find device."));

        var result = new SubscriptionDto();
        result.setDeviceInfo(device.getDeviceInfo());
        result.setLanguage(device.getLanguage());
        result.setPushToken(device.getPushToken());
        result.setTimezoneOffset(device.getTimezoneOffset());
        result.setUserId(device.getUserId());
        result.setSelectedBwConditions(device.getSelectedBwConditions());

        result.setNotificationInfo(getNotificationInfo(pushToken));

        return result;
    }

    private List<NotificationInfoDto> getNotificationInfo(String pushToken) {
        var subs = subscriptionRepository.findByPushToken(pushToken);

        // Group subscriptions by locationId using a map
        Map<String, List<NotificationType>> locationNotificationMap = subs.stream()
            .collect(Collectors.groupingBy(
                NotificationSubscriptionEntity::getLocationId,
                Collectors.mapping(NotificationSubscriptionEntity::getNotificationType, Collectors.toList())
            ));

        // Convert map to NotificationInfoDto list
        return locationNotificationMap.entrySet().stream()
            .map(entry -> {
                var notificationInfo = new NotificationInfoDto();
                notificationInfo.setLocationId(entry.getKey());
                notificationInfo.setLocationName(meteoNewsDataService.getLocationById(entry.getKey(), MeteoNewsDataService.DEFAULT_LANGUAGE).getName());
                notificationInfo.setNotificationTypes(entry.getValue());
                return notificationInfo;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public List<DeviceDto> getDevices(String query) {
        var entities = StringUtils.hasText(query)
            ? deviceRepository.findAllByDeviceInfoContainingIgnoreCaseOrderByUpdatedTimestampDesc(query)
            :deviceRepository.findAllByOrderByUpdatedTimestampDesc();

        return dashboardMapper.toDeviceDtoList(entities);
    }

}
