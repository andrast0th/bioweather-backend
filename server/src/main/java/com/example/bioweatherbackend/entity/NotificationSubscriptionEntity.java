package com.example.bioweatherbackend.entity;

import com.example.bioweatherbackend.dto.notifications.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification_subscription")
@IdClass(NotificationSubscriptionId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationSubscriptionEntity {
    @Id
    @Column(name = "push_token", nullable = false, length = 200)
    private String pushToken;

    @Id
    @Column(name = "notification_type", nullable = false, length = 200)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Id
    @Column(name = "location_id", nullable = false, length = 1000)
    private String locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "push_token", referencedColumnName = "push_token", insertable = false, updatable = false)
    private DeviceEntity device;
}