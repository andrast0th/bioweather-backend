package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.model.notifications.SubscriptionDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationJobService {

    private NotificationService notificationService;

    @Scheduled(cron = "0 0 * * * *") // every hour at minute 0
    public void runHourlyJob() {
        // for now just send an expo notification to all subscriptions every hour or so
        List<SubscriptionDto> subscriptionDtos = notificationService.getAllSubscriptions();
        List<String> tokens = subscriptionDtos.stream().map(SubscriptionDto::getPushToken).toList();

        log.info("Running hourly job for {} subscriptions", tokens.size());

        notificationService.sendDataOnlyNotification();



        log.info("Finished hourly job for {} subscriptions", tokens.size());
    }
}