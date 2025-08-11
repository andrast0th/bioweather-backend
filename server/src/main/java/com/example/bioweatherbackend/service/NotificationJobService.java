package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.dto.notifications.NotificationType;
import com.example.bioweatherbackend.dto.weather.ApiBioWeatherByDate;
import com.example.bioweatherbackend.dto.weather.ApiLocation;
import com.example.bioweatherbackend.entity.DeviceEntity;
import com.example.bioweatherbackend.repository.DeviceRepository;
import com.example.bioweatherbackend.repository.NotificationSubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationJobService {


    private final DeviceRepository deviceRepo;
    private final NotificationSubscriptionRepository subscriptionRepo;
    private final MeteoNewsDataService meteoNewsDataService;

    @Scheduled(cron = "* */15 * * * *", zone = "UTC")
    @Transactional
    public void scheduledRunWorkJob() {
        log.info("Running scheduled job send notifications");

        List<DeviceEntity> devices = deviceRepo.findAllNotDisabled();

        for (DeviceEntity device : devices) {
            var subs = device.getSubscriptions();

            subs.forEach(sub -> {
                log.info("Processing subscription for device: {}, location: {}, type: {}",
                        device.getPushToken(), sub.getLocationId(), sub.getNotificationType());

                if (sub.getLocationId() == null) {
                    return;
                }

                if (NotificationType.BW_TODAY == sub.getNotificationType()) {
                    ApiLocation location = meteoNewsDataService.getLocationById(sub.getLocationId());
                    LocalDate date = getDateForLocation(location);
                    List<ApiBioWeatherByDate> scales = meteoNewsDataService.getScalesByLocationId(sub.getLocationId());
                    var scale = scales.stream().filter(s -> Objects.equals(s.getDate(), date.toString())).findAny();

                    scale.ifPresent(apiBioWeatherByDate -> {
                    });
                    System.out.println(scale);
                }
                // Here you would typically send the notification
                // For example, using a notification service
                // notificationService.sendNotification(device.getPushToken(), subs.getNotificationType());
            });
        }

        log.info("Finished scheduled job to send work notifications");
    }

    LocalDate getDateForLocation(ApiLocation location) {
        // Implement logic to determine the date for the location
        // This could be based on the current date, or some other logic
        int offset = location.getUtcOffset();
        var offsetUnit = location.getUtcOffsetUnit();

        return switch (offsetUnit) {
            case "h" -> LocalDate.now(ZoneOffset.ofHours(offset));
            case "m" -> LocalDate.now(ZoneOffset.ofTotalSeconds(offset * 60));
            case "s" -> LocalDate.now(ZoneOffset.ofTotalSeconds(offset));
            default -> throw new IllegalArgumentException("Unsupported UTC offset unit: " + offsetUnit);
        };
    }

}