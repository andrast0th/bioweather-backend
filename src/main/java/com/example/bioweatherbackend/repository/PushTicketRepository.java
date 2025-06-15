package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.PushTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PushTicketRepository extends JpaRepository<PushTicketEntity, String> {
    List<PushTicketEntity> findAllByWasReceiptCheckedIsFalse();

    @Query("SELECT p FROM PushTicketEntity p WHERE p.wasReceiptChecked = false AND p.notificationSubscription.pushToken IN :pushTokens")
    List<PushTicketEntity> findAllNotCheckedByPushTokens(List<String> pushTokens);
}