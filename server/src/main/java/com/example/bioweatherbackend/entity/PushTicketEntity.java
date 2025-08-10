package com.example.bioweatherbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "push_ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushTicketEntity {

    @Id
    @Column(length = 200, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "push_token", referencedColumnName = "push_token", nullable = false)
    private NotificationSubscriptionEntity notificationSubscription;

    @Column(length = 20)
    private String receiptStatus;

    @Column(length = 100)
    private String receiptError;

    @Column(name = "was_receipt_checked", nullable = false)
    private boolean wasReceiptChecked = false;

    @Column(name = "receipt_checked_at")
    private Instant receiptCheckedAt;

    @Column(name = "ticket_created_at")
    private Instant ticketCreatedAt;
}