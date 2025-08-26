package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.dto.notifications.NotificationType;
import com.example.bioweatherbackend.dto.notifications.PushTicketDto;
import com.example.bioweatherbackend.dto.notifications.SubscriptionDto;
import com.example.bioweatherbackend.dto.notifications.TestNotificationDto;
import com.example.bioweatherbackend.service.DeviceManagementService;
import com.example.bioweatherbackend.service.NotificationJobService;
import com.example.bioweatherbackend.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@AllArgsConstructor
public class NotificationsController {

    private DeviceManagementService service;
    private NotificationService notificationService;
    private NotificationJobService jobService;

    @PostMapping("subscription")
    public void subscribe(@RequestBody SubscriptionDto subscription) {
        service.subscribe(subscription);
    }

    @DeleteMapping("subscription")
    public void unsubscribe(@RequestBody SubscriptionDto subscription) {
        service.unsubscribe(subscription);
    }

    @GetMapping("/subscription/{pushToken}")
    public List<SubscriptionDto> getSubscriptionsByPushToken(@PathVariable String pushToken) {
        return service.getSubscriptions(pushToken);
    }

    @GetMapping("/history/{pushToken}")
    public List<PushTicketDto> getHistory(@PathVariable String pushToken, @RequestParam(required = false) String locationId) {
        return notificationService.getNotificationHistory(pushToken, locationId);
    }

    @PostMapping("/test-notification")
    public void triggerNotification(@RequestBody TestNotificationDto notificationDto) {
        notificationService.sendTextNotification(notificationDto.getPushToken(), "Test Notification", notificationDto.getMessage(), NotificationType.TEST, null);
    }

    @PostMapping("/test-job")
    public void triggerJob() {
        jobService.scheduledRunWorkJob();
    }


}
