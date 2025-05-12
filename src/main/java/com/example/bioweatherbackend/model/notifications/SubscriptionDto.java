package com.example.bioweatherbackend.model.notifications;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionDto {
    private String pushToken;
    private String userId;
    private String deviceId;
    private String locationId;
    private String notificationType;
}
