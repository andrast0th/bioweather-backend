package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.model.notifications.SubscriptionDto;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationJobService {

    private NotificationService notificationService;
    private ExpoNotificationsService expoNotificationsService;

    @Scheduled(cron = "0 0 * * * *") // every hour at minute 0
    public void runHourlyJob() {
        // for now just send an expo notification to all subscriptions every hour or so
        List<SubscriptionDto> subscriptionDtos = notificationService.getAllSubscriptions();
        subscriptionDtos.forEach(notificationDto -> expoNotificationsService.sendNotification(notificationDto.getPushToken()));
    }
}