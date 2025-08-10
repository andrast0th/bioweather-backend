package com.example.bioweatherbackend.entity;

import com.example.bioweatherbackend.dto.notifications.NotificationType;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSubscriptionId implements Serializable {
    private String pushToken;
    private NotificationType notificationType;
    private String locationId;
}