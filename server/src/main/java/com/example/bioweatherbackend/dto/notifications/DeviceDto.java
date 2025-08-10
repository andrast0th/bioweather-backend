package com.example.bioweatherbackend.dto.notifications;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
public class DeviceDto {
    private String pushToken;
    private String userId;
    private Map<String, String> deviceInfo;
    private String language;
    private int timezoneOffset;
    private Instant updatedTimestamp;
}
