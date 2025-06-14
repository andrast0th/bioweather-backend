package com.example.bioweatherbackend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationJobService {

    private ExpoNotificationService notificationService;

    @Scheduled(cron = "0 0 */4 * * *")
    public void runScheduleWorkJob() {
        log.info("Running scheduled job to send work notifications");
        notificationService.sendScheduleWorkNotifications();
        log.info("Finished scheduled job to send work notifications");
    }

    @Scheduled(cron = "0 30 */4 * * *")
    public void runPushTicketCheckJob() {
        log.info("Running scheduled job to check push tickets");
        notificationService.handleSavedPushTickets();
        log.info("Finished scheduled to check push tickets");
    }
}