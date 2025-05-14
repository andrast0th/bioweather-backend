package com.example.bioweatherbackend.api;

import com.example.bioweatherbackend.model.notifications.NotificationDto;
import com.example.bioweatherbackend.model.notifications.SubscriptionDto;
import com.example.bioweatherbackend.sec.RequireAuth;
import com.example.bioweatherbackend.service.NotificationJobService;
import com.example.bioweatherbackend.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
public class NotificationsController {

    private NotificationService service;
    private NotificationJobService jobService;

    @PostMapping("subscription")
    public void subscribe(@RequestBody SubscriptionDto subscription) {
        service.subscribe(subscription);
    }

    @DeleteMapping("subscription")
    public void unsubscribe(@RequestBody SubscriptionDto subscription) {
        service.unsubscribe(subscription);
    }

    @PostMapping("/manual")
    @RequireAuth
    public void triggerNotification(@RequestBody NotificationDto notificationDto) {
        service.sendNotification(notificationDto.getTitle(), notificationDto.getSubtitle());
    }

    @PostMapping("/work")
    @RequireAuth
    public void triggerWork() {
        jobService.runHourlyJob();
    }

}
