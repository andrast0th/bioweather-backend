package com.example.bioweatherbackend.dto.notifications;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnsubscribeDto {
    private String pushToken;
    private String locationId;
}
