package com.example.bioweatherbackend.dto.notifications;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class NotificationSubscriptionDto {
    private String pushToken;
    private String userId;
    private String deviceInfo;
    private ZonedDateTime updatedViaSubscribe;
}
