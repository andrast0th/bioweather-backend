package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.dto.ConfigDto;
import com.example.bioweatherbackend.entity.ConfigEntity;
import com.example.bioweatherbackend.repository.ConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfigService {

    private final ConfigRepository configRepository;

    public ConfigDto getConfig() {
        ConfigDto dto = new ConfigDto();
        configRepository.findById("notificationJobCron").ifPresentOrElse(config -> dto.setNotificationJobCron(config.getValue()), () -> dto.setNotificationJobCron("0 */15 * * * *"));
        configRepository.findById("notificationThresholdMinutes").ifPresentOrElse(config -> dto.setNotificationThresholdMinutes(Integer.parseInt(config.getValue())), () -> dto.setNotificationThresholdMinutes(10));
        configRepository.findById("bwTodayNotificationHour").ifPresentOrElse(config -> dto.setBwTodayNotificationHour(config.getValue()), () -> dto.setBwTodayNotificationHour("9:00"));
        configRepository.findById("bwTomorrowNotificationHour").ifPresentOrElse(config -> dto.setBwTomorrowNotificationHour(config.getValue()), () -> dto.setBwTomorrowNotificationHour("18:00"));
        return dto;
    }

    public void updateConfig(ConfigDto configDto) {
        saveOrUpdate("notificationJobCron", configDto.getNotificationJobCron());
        saveOrUpdate("notificationThresholdMinutes", String.valueOf(configDto.getNotificationThresholdMinutes()));
        saveOrUpdate("bwTodayNotificationHour", configDto.getBwTodayNotificationHour());
        saveOrUpdate("bwTomorrowNotificationHour", configDto.getBwTomorrowNotificationHour());
    }

    private void saveOrUpdate(String id, String value) {
        configRepository.findById(id).ifPresentOrElse(config -> {
            config.setValue(value);
            configRepository.save(config);
        }, () -> {
            ConfigEntity entity = new ConfigEntity();
            entity.setId(id);
            entity.setValue(value);
            configRepository.save(entity);
        });
    }
}
