package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.PushTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface PushTicketRepository extends JpaRepository<PushTicketEntity, String> {
    List<PushTicketEntity> findAllByWasReceiptCheckedIsFalse();

    @Query("SELECT p FROM PushTicketEntity p WHERE p.wasReceiptChecked = false AND p.notificationSubscription.pushToken IN :pushTokens")
    List<PushTicketEntity> findAllNotCheckedByPushTokens(List<String> pushTokens);

    @Query("SELECT p.notificationSubscription.pushToken FROM PushTicketEntity p " +
           "WHERE p.receiptStatus = 'ERROR' AND p.receiptCheckedAt > :sinceDate " +
           "GROUP BY p.notificationSubscription.pushToken " +
           "HAVING COUNT(p) > :errorCount")
    List<String> findPushTokensWithErrorsSince(int errorCount, Instant sinceDate);

    // find all tickets by push token order by checked at date
    @Query("SELECT p FROM PushTicketEntity p WHERE p.notificationSubscription.pushToken = :pushToken ORDER BY p.receiptCheckedAt DESC")
    List<PushTicketEntity> findAllByPushToken(String pushToken);
}