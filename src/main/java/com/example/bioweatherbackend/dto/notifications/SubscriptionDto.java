package com.example.bioweatherbackend.dto.notifications;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionDto {
    private String pushToken;
    private String userId;
    private String deviceInfo;
}
