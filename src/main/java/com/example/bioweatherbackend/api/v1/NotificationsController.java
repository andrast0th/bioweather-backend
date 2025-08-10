package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.dto.notifications.SubscriptionDto;
import com.example.bioweatherbackend.service.DeviceManagementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@AllArgsConstructor
public class NotificationsController {

    private DeviceManagementService service;

    @PostMapping("subscription")
    public void subscribe(@RequestBody SubscriptionDto subscription) {
        service.subscribe(subscription);
    }

    @DeleteMapping("subscription")
    public void unsubscribe(@RequestBody SubscriptionDto subscription) {
        service.unsubscribe(subscription);
    }

//    @GetMapping("/subscription")
//    @RequireAuth
//    public List<NotificationSubscriptionDto> runPushTicketCheckJob() {
//        return service.getAllNotificationSubscriptions();
//    }
//
//    @PostMapping("/manual")
//    @RequireAuth
//    public void triggerNotification(@RequestBody NotificationDto notificationDto) {
//        service.sendTextNotification(notificationDto.getTitle(), notificationDto.getSubtitle());
//    }
//
//    @PostMapping("/schedule-work")
//    @RequireAuth
//    public void runScheduleWorkJob(@RequestParam("pushTokens") List<String> pushTokens) {
//        jobService.runScheduleWorkJob(pushTokens);
//    }
//
//    @PostMapping("/push-receipts")
//    @RequireAuth
//    public void runPushTicketCheckJob(@RequestParam("pushTokens") List<String> pushTokens) {
//        jobService.runPushTicketCheckJob(pushTokens);
//    }

}
