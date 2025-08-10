package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.NotificationSubscriptionEntity;
import com.example.bioweatherbackend.entity.NotificationSubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscriptionEntity, NotificationSubscriptionId> {
    void deleteByPushTokenAndLocationId(String pushToken, String locationId);
}