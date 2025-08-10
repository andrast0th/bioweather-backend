package com.example.bioweatherbackend.dto.notifications;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestNotificationDto {
    String pushToken;
    String message;
}