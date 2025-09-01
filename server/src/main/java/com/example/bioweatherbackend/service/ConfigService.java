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
        configRepository.findById("notificationJobCron")
            .ifPresentOrElse(config -> dto.setNotificationJobCron(config.getValue()), () -> dto.setNotificationJobCron("0 */15 * * * *"));

        configRepository.findById("bwNotificationThresholdMinMinutes")
            .ifPresentOrElse(config -> dto.setBwNotificationThresholdMinMinutes(Integer.parseInt(config.getValue())), () -> dto.setBwNotificationThresholdMinMinutes(-5));
        configRepository.findById("bwNotificationThresholdMaxMinutes")
            .ifPresentOrElse(config -> dto.setBwNotificationThresholdMaxMinutes(Integer.parseInt(config.getValue())), () -> dto.setBwNotificationThresholdMaxMinutes(10));

        configRepository.findById("crNotificationThresholdMinMinutes")
            .ifPresentOrElse(config -> dto.setCrNotificationThresholdMinMinutes(Integer.parseInt(config.getValue())), () -> dto.setCrNotificationThresholdMinMinutes(-15));
        configRepository.findById("crNotificationThresholdMaxMinutes")
            .ifPresentOrElse(config -> dto.setCrNotificationThresholdMaxMinutes(Integer.parseInt(config.getValue())), () -> dto.setCrNotificationThresholdMaxMinutes(-10));

        configRepository.findById("bwTodayNotificationHour")
            .ifPresentOrElse(config -> dto.setBwTodayNotificationHour(config.getValue()), () -> dto.setBwTodayNotificationHour("9:00"));
        configRepository.findById("bwTomorrowNotificationHour")
            .ifPresentOrElse(config -> dto.setBwTomorrowNotificationHour(config.getValue()), () -> dto.setBwTomorrowNotificationHour("18:00"));
        return dto;
    }

    public void updateConfig(ConfigDto configDto) {
        saveOrUpdate("notificationJobCron", configDto.getNotificationJobCron());
        saveOrUpdate("bwNotificationThresholdMinMinutes", String.valueOf(configDto.getBwNotificationThresholdMinMinutes()));
        saveOrUpdate("bwNotificationThresholdMaxMinutes", String.valueOf(configDto.getBwNotificationThresholdMaxMinutes()));
        saveOrUpdate("crNotificationThresholdMinMinutes", String.valueOf(configDto.getCrNotificationThresholdMinMinutes()));
        saveOrUpdate("crNotificationThresholdMaxMinutes", String.valueOf(configDto.getCrNotificationThresholdMaxMinutes()));
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
