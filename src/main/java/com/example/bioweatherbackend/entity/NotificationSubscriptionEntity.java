package com.example.bioweatherbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification_subscription")
@Getter
@Setter
public class NotificationSubscriptionEntity {
    @EmbeddedId
    private NotificationSubscriptionId id;

    @Column(name = "user_id", nullable = false, length = 200)
    private String userId;
    @Column(name = "device_id", nullable = false, length = 200)
    private String deviceId;
    @Column(name = "notification_type", nullable = false, length = 200)
    private String notificationType;
}
