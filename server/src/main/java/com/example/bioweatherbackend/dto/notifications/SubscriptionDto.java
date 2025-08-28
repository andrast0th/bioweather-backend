package com.example.bioweatherbackend.dto.notifications;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubscriptionDto {
    private String pushToken;
    private String userId;
    private String deviceInfo;
    private String language;
    private int timezoneOffset;
    List<NotificationInfoDto> notificationInfo;
    private List<String> selectedBwConditions;
}
