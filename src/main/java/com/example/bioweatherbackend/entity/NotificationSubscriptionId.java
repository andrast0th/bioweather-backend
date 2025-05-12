package com.example.bioweatherbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSubscriptionId implements Serializable {
    @Column(name = "push_token", nullable = false, length = 200)
    private String pushToken;

    @Column(name = "location_id", nullable = false, length = 200)
    private String locationId;
}