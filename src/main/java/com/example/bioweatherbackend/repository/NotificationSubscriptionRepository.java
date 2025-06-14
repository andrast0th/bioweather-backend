package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.NotificationSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscriptionEntity, String> {
}