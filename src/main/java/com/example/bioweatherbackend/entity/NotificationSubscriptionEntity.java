package com.example.bioweatherbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "notification_subscription")
@Getter
@Setter
public class NotificationSubscriptionEntity {
    @Id
    @Column(name = "push_token", nullable = false, length = 200)
    private String pushToken;

    @Column(name = "user_id", nullable = false, length = 200)
    private String userId;

    @Column(name = "device_info", nullable = false, length = 1000)
    private String deviceInfo;

    @Column(name = "updated_via_subscribe")
    private Instant updatedViaSubscribe;
}
