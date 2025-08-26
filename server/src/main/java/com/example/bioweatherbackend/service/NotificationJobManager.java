package com.example.bioweatherbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class NotificationJobManager {

    private final TaskScheduler taskScheduler;
    private final NotificationJobService notificationJobService;
    private final AtomicReference<ScheduledFuture<?>> scheduledFuture = new AtomicReference<>();
    private final AtomicReference<String> cronExpression = new AtomicReference<>("0 */15 * * * *");
    private final ConfigService configService;

    @Autowired
    public NotificationJobManager(TaskScheduler taskScheduler, NotificationJobService notificationJobService, ConfigService configService) {
        this.taskScheduler = taskScheduler;
        this.notificationJobService = notificationJobService;
        this.configService = configService;
        scheduleTask(cronExpression.get());
    }

    @Scheduled(fixedRate = 10000)
    public void updateCronExpression() {
        String oldCron = cronExpression.get();
        String dbCron = configService.getConfig().getNotificationJobCron();

        if (!oldCron.equals(dbCron)) {
            cronExpression.set(dbCron);
            rescheduleTask();
        }
    }

    private void scheduleTask(String cron) {
        ScheduledFuture<?> future = taskScheduler.schedule(notificationJobService::scheduledRunWorkJob, new CronTrigger(cron, ZoneId.of("UTC")));
        scheduledFuture.set(future);
    }

    private void rescheduleTask() {
        ScheduledFuture<?> future = scheduledFuture.getAndSet(null);
        if (future != null) {
            future.cancel(false);
        }
        scheduleTask(cronExpression.get());
    }
}