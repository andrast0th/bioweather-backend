package com.example.bioweatherbackend.api;

import com.example.bioweatherbackend.model.notifications.SubscriptionDto;
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

    @PostMapping
    public void subscribe(@RequestBody SubscriptionDto subscription) {
        service.subscribe(subscription);
    }

    @DeleteMapping
    public void unsubscribe(@RequestBody SubscriptionDto subscription) {
        service.unsubscribe(subscription);
    }

    @PostMapping("/trigger-all")
    public void trigger() {
        jobService.runHourlyJob();
    }

}
