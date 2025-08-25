package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.dto.notifications.NotificationType;
import com.example.bioweatherbackend.dto.weather.ApiLocation;
import com.example.bioweatherbackend.entity.DeviceEntity;
import com.example.bioweatherbackend.repository.DeviceRepository;
import com.example.bioweatherbackend.service.meteo.CircadianRhythmService;
import com.example.bioweatherbackend.service.meteo.DateTimeUtil;
import com.example.bioweatherbackend.service.meteo.MeteoNewsDataService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationJobService {

    private static final DateTimeFormatter[] TIME_FORMATTERS = {
        DateTimeFormatter.ofPattern("H:mm"),
        DateTimeFormatter.ofPattern("HH:mm")
    };

    private final DeviceRepository deviceRepo;
    private final NotificationService notificationService;
    private final TranslationService translationService;
    private final MeteoNewsDataService meteoNewsDataService;
    private final CircadianRhythmService circadianRhythmService;
    private final ConfigService configService;
    private final DateTimeUtil dateTimeUtil;

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
                var datetimeLocation = dateTimeUtil.getDateTimeForLocation(location);

                var localTimeBwToday = parseDbTime(configService.getConfig().getBwTodayNotificationHour());
                var localTimeBwTomorrow = parseDbTime(configService.getConfig().getBwTomorrowNotificationHour());
                var notificationThresholdMinutes = configService.getConfig().getNotificationThresholdMinutes();

                try {
                    if (NotificationType.BW_TODAY == sub.getNotificationType() &&
                        isInTimeRange(datetimeLocation, localTimeBwToday, Duration.ofMinutes(notificationThresholdMinutes), Duration.ofMinutes(notificationThresholdMinutes))) {
                        sendBwNotification(device, location, sub.getNotificationType(), datetimeLocation);
                    } else if (NotificationType.BW_TOMORROW == sub.getNotificationType() &&
                        isInTimeRange(datetimeLocation, localTimeBwTomorrow, Duration.ofMinutes(notificationThresholdMinutes), Duration.ofMinutes(notificationThresholdMinutes))) {
                        sendBwNotification(device, location, sub.getNotificationType(), datetimeLocation);
                    } else {
                        checkAndSendCrNotification(device, location, sub.getNotificationType(), datetimeLocation);
                    }
                } catch (Exception e) {
                    log.error("Error sending notification for device: {}, location: {}, type: {}", device.getPushToken(), sub.getLocationId(), sub.getNotificationType(), e);
                }

            });
        }

        log.info("Finished scheduled job to send work notifications");
    }

    private void checkAndSendCrNotification(DeviceEntity device, ApiLocation location, NotificationType notificationType, ZonedDateTime datetimeLocation) {
        var cr = circadianRhythmService.getCircadianRhythm(location.getId(), datetimeLocation.toLocalDate());

        var crMap = new HashMap<NotificationType, LocalDateTime>();
        crMap.put(NotificationType.BEDTIME, cr.getBed());
        crMap.put(NotificationType.WAKEUP, cr.getWakeUp());
        crMap.put(NotificationType.LAST_MEAL, cr.getLastMeal());
        crMap.put(NotificationType.NEXT_REST, cr.getNextRestPeriodStart());
        crMap.put(NotificationType.PEAK, cr.getPeakAlertnessStart());
        crMap.put(NotificationType.EXERCISE, cr.getExerciseStart());

        crMap.forEach((crKey, crValue) -> {
            if (notificationType == crKey && isInTimeRange(datetimeLocation, crValue.toLocalTime(), Duration.ofMinutes(configService.getConfig().getNotificationThresholdMinutes()), Duration.ofMinutes(configService.getConfig().getNotificationThresholdMinutes()))) {
                log.info("Sending circadian rhythm notification for device: {}, location: {}, type: {}", device.getPushToken(), location.getId(), notificationType);
                var translations = translationService.getTranslationsMap(device.getLanguage());

                var title = translations.get("notifications." + crKey.getValue() + ".title");
                title = title.replace("%{locationName}", location.getName());

                var content = translations.get("notifications." + crKey.getValue() + ".body");
                content = content.replace("%{locationName}", location.getName());

                notificationService.sendTextNotification(device.getPushToken(), title, content, notificationType, location.getId());
            }
        });

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

        var alertConditions = conditions.stream().filter(c -> c.getSeverity() > 2).toList();
        var selectedConditions = device.getSelectedBwConditions() == null ? List.of() : device.getSelectedBwConditions();
        alertConditions = alertConditions.stream().filter(c -> selectedConditions.contains(c.getCondition())).toList();

        if (alertConditions.isEmpty()) {
            log.info("No alert conditions for device: {}, location: {}, date: {}", device.getPushToken(), location.getId(), targetDate);
            return;
        }

        var translations = translationService.getTranslationsMap(device.getLanguage());

        List<String> alertConditionsNames = alertConditions.stream().map(condition -> translations.get("bw.conditions." + condition.getCondition() + ".name")).toList();

        String title = translations.get("notifications.bw.title");
        title = title.replace("%{locationName}", location.getName());
        title = title.replace("%{dayStr}", targetDate.toString());

        String body = translations.get("notifications.bw.body");
        body = body.replace("%{conditionsStr}", String.join(", ", alertConditionsNames));

        notificationService.sendTextNotification(device.getPushToken(), title, body, notificationType, location.getId());
    }


    private boolean isInTimeRange(ZonedDateTime zonedDateTime, LocalTime targetTime, Duration startOffset, Duration endOffset) {
        ZonedDateTime startTime = zonedDateTime.with(targetTime).minus(startOffset);
        ZonedDateTime endTime = zonedDateTime.with(targetTime).plus(endOffset);

        ZonedDateTime now = ZonedDateTime.now(zonedDateTime.getZone());

        return !now.isBefore(startTime) && !now.isAfter(endTime);
    }

    private LocalTime parseDbTime(String timeStr) {
        for (DateTimeFormatter formatter : TIME_FORMATTERS) {
            try {
                return LocalTime.parse(timeStr, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new IllegalArgumentException("Invalid time format: " + timeStr);
    }

}