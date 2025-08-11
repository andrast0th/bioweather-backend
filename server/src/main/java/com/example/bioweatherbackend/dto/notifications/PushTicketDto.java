package com.example.bioweatherbackend.dto.notifications;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PushTicketDto {
    private String id;
    private String pushToken;
    private String receiptStatus;
    private NotificationType notificationType;
    private String notificationTitle;
    private String notificationBody;
    private String locationId;
    private String receiptError;
    private boolean wasReceiptChecked = false;
    private Instant receiptCheckedAt;
    private Instant ticketCreatedAt;
}
