package com.example.bioweatherbackend.dto.notifications;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NotificationInfoDto {
    
    private String locationId;
    private String locationName;
    private List<NotificationType> notificationTypes = new ArrayList<>();

}
