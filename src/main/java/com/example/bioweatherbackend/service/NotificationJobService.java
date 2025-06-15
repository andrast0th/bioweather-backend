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
        notificationService.handleSavedPushTickets(pushTokens);
    }

    @Scheduled(cron = "0 0 */4 * * *")
    public void scheduledRunWorkJob() {
        log.info("Running scheduled job to send work notifications");
        notificationService.sendScheduleWorkNotifications();
        log.info("Finished scheduled job to send work notifications");
    }

    @Scheduled(cron = "0 30 */4 * * *")
    public void scheduledRunPushTicketCheckJob() {
        log.info("Running scheduled job to check push tickets");
        notificationService.handleSavedPushTickets();
        log.info("Finished scheduled to check push tickets");
    }

}