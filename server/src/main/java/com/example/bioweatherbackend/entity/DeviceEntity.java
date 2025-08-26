package com.example.bioweatherbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "device")
@Getter
@Setter
public class DeviceEntity {
    @Id
    @Column(name = "push_token", nullable = false, length = 200)
    private String pushToken;

    @Column(name = "user_id", nullable = false, length = 200)
    private String userId;

    @Column(name = "device_info", length = 1000)
    private String deviceInfo;

    @Column(name = "updated_timestamp")
    private Instant updatedTimestamp;

    @Column(name = "is_disabled", nullable = false)
    private boolean isDisabled = false;

    @Column(name = "language", nullable = false, length = 10)
    private String language = "en";

    @Column(name = "timezone_offset", nullable = false)
    private Integer timezoneOffset = 0;

    @Column(name = "selected_bw_conditions", columnDefinition = "text[]")
    private List<String> selectedBwConditions;

    @OneToMany(mappedBy = "pushToken", fetch = FetchType.LAZY)
    private List<NotificationSubscriptionEntity> subscriptions;
}
