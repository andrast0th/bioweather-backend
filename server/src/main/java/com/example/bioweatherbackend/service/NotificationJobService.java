package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.dto.notifications.NotificationType;
import com.example.bioweatherbackend.dto.weather.ApiLocation;
import com.example.bioweatherbackend.entity.DeviceEntity;
import com.example.bioweatherbackend.repository.DeviceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationJobService {


    private final DeviceRepository deviceRepo;
    private final NotificationService notificationService;
    private final TranslationService translationService;

    private final MeteoNewsDataService meteoNewsDataService;

    @Scheduled(cron = "0 */15 * * * *", zone = "UTC")
    @Transactional
    public void scheduledRunWorkJob() {
        log.info("Running scheduled job send notifications");

        List<DeviceEntity> devices = deviceRepo.findAllNotDisabled();

        for (DeviceEntity device : devices) {
            var subs = device.getSubscriptions();

            subs.forEach(sub -> {
                log.info("Processing subscription for device: {}, location: {}, type: {}", device.getPushToken(), sub.getLocationId(), sub.getNotificationType());

                if (sub.getLocationId() == null) {
                    return;
                }

                ApiLocation location = meteoNewsDataService.getLocationById(sub.getLocationId());
                var datetimeLocation = getDateTimeForLocation(location);

                try {
                    if (NotificationType.BW_TODAY == sub.getNotificationType() && isInTimeRange(datetimeLocation, LocalTime.of(8, 0), Duration.ofMinutes(10), Duration.ofMinutes(10))) {
                        sendBwNotification(device, location, sub.getNotificationType(), datetimeLocation);
                    } else if (NotificationType.BW_TOMORROW == sub.getNotificationType() && isInTimeRange(datetimeLocation, LocalTime.of(18, 0), Duration.ofMinutes(10), Duration.ofMinutes(10))) {
                        sendBwNotification(device, location, sub.getNotificationType(), datetimeLocation);
                    }
                } catch (Exception e) {
                    log.error("Error sending notification for device: {}, location: {}, type: {}", device.getPushToken(), sub.getLocationId(), sub.getNotificationType(), e);
                }

            });
        }

        log.info("Finished scheduled job to send work notifications");
    }

    private void sendBwNotification(DeviceEntity device, ApiLocation location, NotificationType notificationType, ZonedDateTime datetimeLocation) {
        // Implement the logic to send the notification
        // This could involve creating a PushTicketEntity and saving it to the repository
        log.info("Sending notification for device: {}, location: {}, type: {}", device.getPushToken(), location.getId(), notificationType);

        var scalesByDates = meteoNewsDataService.getScalesByLocationId(location.getId());
        LocalDate targetDate;

        if (notificationType == NotificationType.BW_TODAY) {
            targetDate = datetimeLocation.toLocalDate();
        } else if (notificationType == NotificationType.BW_TOMORROW) {
            targetDate = datetimeLocation.toLocalDate().plusDays(1);
        } else {
            throw new IllegalStateException("Unexpected BW notification type: " + notificationType);
        }


        var scales = scalesByDates.stream().filter(s -> Objects.equals(s.getDate(), targetDate.toString())).findAny().orElse(null);

        if (scales == null) {
            log.warn("No scales found for location: {}, date: {}", location.getId(), datetimeLocation.toLocalDate());
            return;
        }

        var conditions = scales.getConditions();

        var alterConditions = conditions.stream().filter(c -> c.getSeverity() > 2).toList();

        var translations = translationService.getTranslationsMap(device.getLanguage());

        List<String> alertConditionsNames = alterConditions.stream().map(condition -> translations.get("bw.conditions." + condition.getCondition() + ".name")).toList();

        String title = translations.get("notifications.bw.title");
        title = title.replace("%{locationName}", location.getName());
        title = title.replace("%{dayStr}", targetDate.toString());

        String body = translations.get("notifications.bw.body");
        body = body.replace("%{conditionsStr}", String.join(", ", alertConditionsNames));

        notificationService.sendTextNotification(device.getPushToken(), title, body, notificationType);
    }

    private ZonedDateTime getDateTimeForLocation(ApiLocation location) {
        // Implement logic to determine the date for the location
        // This could be based on the current date, or some other logic
        int offset = location.getUtcOffset();
        var offsetUnit = location.getUtcOffsetUnit();

        return switch (offsetUnit) {
            case "h" -> ZonedDateTime.now(ZoneOffset.ofHours(offset));
            case "m" -> ZonedDateTime.now(ZoneOffset.ofTotalSeconds(offset * 60));
            case "s" -> ZonedDateTime.now(ZoneOffset.ofTotalSeconds(offset));
            default -> throw new IllegalArgumentException("Unsupported UTC offset unit: " + offsetUnit);
        };
    }

    private boolean isInTimeRange(ZonedDateTime zonedDateTime, LocalTime targetTime, Duration startOffset, Duration endOffset) {
        ZonedDateTime startTime = zonedDateTime.with(targetTime).minus(startOffset);
        ZonedDateTime endTime = zonedDateTime.with(targetTime).plus(endOffset);

        ZonedDateTime now = ZonedDateTime.now(zonedDateTime.getZone());

        return !now.isBefore(startTime) && !now.isAfter(endTime);
    }

}