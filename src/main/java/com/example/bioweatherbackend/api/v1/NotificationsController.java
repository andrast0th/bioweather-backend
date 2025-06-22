package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.config.sec.RequireAuth;
import com.example.bioweatherbackend.dto.notifications.NotificationDto;
import com.example.bioweatherbackend.dto.notifications.NotificationSubscriptionDto;
import com.example.bioweatherbackend.dto.notifications.SubscriptionDto;
import com.example.bioweatherbackend.service.ExpoNotificationService;
import com.example.bioweatherbackend.service.NotificationJobService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
public class NotificationsController {

    private ExpoNotificationService service;
    private NotificationJobService jobService;

    @PostMapping("subscription")
    public void subscribe(@RequestBody SubscriptionDto subscription) {
        service.subscribe(subscription);
    }

    @DeleteMapping("subscription")
    public void unsubscribe(@RequestBody SubscriptionDto subscription) {
        service.unsubscribe(subscription);
    }

    @GetMapping("/subscription")
    @RequireAuth
    public List<NotificationSubscriptionDto> runPushTicketCheckJob() {
        return service.getAllNotificationSubscriptions();
    }

    @PostMapping("/manual")
    @RequireAuth
    public void triggerNotification(@RequestBody NotificationDto notificationDto) {
        service.sendTextNotification(notificationDto.getTitle(), notificationDto.getSubtitle());
    }

    @PostMapping("/schedule-work")
    @RequireAuth
    public void runScheduleWorkJob(@RequestParam("pushTokens") List<String> pushTokens) {
        jobService.runScheduleWorkJob(pushTokens);
    }

    @PostMapping("/push-receipts")
    @RequireAuth
    public void runPushTicketCheckJob(@RequestParam("pushTokens") List<String> pushTokens) {
        jobService.runPushTicketCheckJob(pushTokens);
    }

}
