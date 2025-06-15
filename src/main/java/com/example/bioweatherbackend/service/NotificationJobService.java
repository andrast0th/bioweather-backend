package com.example.bioweatherbackend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationJobService {

    private ExpoNotificationService notificationService;

    public void runScheduleWorkJob(List<String> pushTokens) {
        notificationService.sendScheduleWorkNotifications(pushTokens);
    }

    public void runPushTicketCheckJob(List<String> pushTokens) {
        notificationService.checkPendingPushTicketReceipts(pushTokens);
    }

    @Scheduled(cron = "0 0 1,3,5,10,15 * * *", zone = "UTC")
    public void scheduledRunWorkJob() {
        log.info("Running scheduled job to send work notifications");
        notificationService.sendScheduleWorkNotifications();
        log.info("Finished scheduled job to send work notifications");
    }

    @Scheduled(cron = "0 0 2,4,11,16 * * *")
    public void scheduledRunPushTicketCheckJob() {
        log.info("Running scheduled job to check push tickets");
        notificationService.checkPendingPushTicketReceipts();
        log.info("Finished scheduled job check push tickets");
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void cleanupPushTokensWithErrors() {
        log.info("Running scheduled job to cleanup push tokens with errors");
        notificationService.cleanupDevicesWithPushReceiptErrors();
        log.info("Finished scheduled job to cleanup push tokens with errors");
    }

}