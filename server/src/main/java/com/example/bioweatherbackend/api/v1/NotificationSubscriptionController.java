package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.dto.notifications.SubscriptionDto;
import com.example.bioweatherbackend.service.DeviceManagementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@AllArgsConstructor
public class NotificationSubscriptionController {

    private DeviceManagementService service;

    @PostMapping("subscription")
    public void subscribe(@RequestBody SubscriptionDto subscription) {
        service.subscribe(subscription);
    }

    @DeleteMapping("subscription")
    public void unsubscribe(@RequestBody SubscriptionDto subscription) {
        service.unsubscribe(subscription);
    }


}
