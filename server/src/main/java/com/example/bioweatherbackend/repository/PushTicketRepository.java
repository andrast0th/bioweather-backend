package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.PushTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface PushTicketRepository extends JpaRepository<PushTicketEntity, String> {
    List<PushTicketEntity> findAllByWasReceiptCheckedIsFalse();

    @Query("SELECT p FROM PushTicketEntity p WHERE p.wasReceiptChecked = false AND p.device.pushToken IN :pushTokens")
    List<PushTicketEntity> findAllNotCheckedByPushTokens(List<String> pushTokens);

    @Query("SELECT p.device.pushToken FROM PushTicketEntity p " + "WHERE p.receiptStatus = 'ERROR' AND p.receiptCheckedAt > :sinceDate " + "GROUP BY p.device.pushToken " + "HAVING COUNT(p) > :errorCount")
    List<String> findPushTokensWithErrorsSince(int errorCount, Instant sinceDate);

    // find all tickets by push token order by checked at date
    @Query("SELECT p FROM PushTicketEntity p WHERE p.device.pushToken = :pushToken ORDER BY p.ticketCreatedAt DESC LIMIT 100")
    List<PushTicketEntity> findAllByPushToken(String pushToken);

    @Query("SELECT p FROM PushTicketEntity p WHERE p.device.pushToken = :pushToken and p.locationId = :locationId ORDER BY p.receiptCheckedAt DESC")
    List<PushTicketEntity> findByPushTokenAndLocation(String pushToken, String locationId);

    @Query("SELECT p FROM PushTicketEntity p " + "WHERE p.device.pushToken = :pushToken " + "AND p.locationId = :locationId " + "AND p.notificationType = :notificationType " + "AND p.ticketCreatedAt >= :startOfDay " + "AND p.ticketCreatedAt < :endOfDay")
    List<PushTicketEntity> findAllByDeviceLocationTypeAndDate(String pushToken, String locationId, com.example.bioweatherbackend.dto.notifications.NotificationType notificationType, Instant startOfDay, Instant endOfDay);
}