package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.dto.expo.PushNotification;
import com.example.bioweatherbackend.dto.expo.Status;
import com.example.bioweatherbackend.dto.expo.TicketResponse;
import com.example.bioweatherbackend.dto.notifications.NotificationType;
import com.example.bioweatherbackend.entity.DeviceEntity;
import com.example.bioweatherbackend.entity.PushTicketEntity;
import com.example.bioweatherbackend.repository.NotificationSubscriptionRepository;
import com.example.bioweatherbackend.repository.PushTicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
public class NotificationService {

    private final RestClient restClient;
    private final PushTicketRepository pushTicketRepository;

    public NotificationService(@Qualifier("expoRestClient") RestClient restClient, NotificationSubscriptionRepository subscriptionRepository, PushTicketRepository pushTicketRepository) {
        this.restClient = restClient;
        this.pushTicketRepository = pushTicketRepository;
    }

    public void sendTestNotification(String pushToken, String title, String subtitle) {
        PushNotification notification = new PushNotification();
        notification.setTo(Collections.singletonList(pushToken));
        notification.setTitle(title);
        notification.setSubtitle(subtitle);
        notification.setBody(subtitle);
        notification.setSound("default");

        TicketResponse response = restClient.post()
                .uri("/push/send")
                .contentType(APPLICATION_JSON)
                .body(notification)
                .retrieve()
                .body(TicketResponse.class);

        if (response != null) {
            handlePushTicketResponse(Collections.singletonList(pushToken), response.getData(), NotificationType.TEST, notification.getTitle(), notification.getBody(), null);
        }
    }


    private void handlePushTicketResponse(List<String> tokens,
                                          List<TicketResponse.Ticket> tickets,
                                          NotificationType notificationType,
                                          String notificationTitle,
                                          String notificationBody,
                                          String locationId) {
        if (tickets == null || tickets.isEmpty()) {
            log.warn("No tickets received from Expo server.");
            return;
        }

        for (TicketResponse.Ticket ticket : tickets) {

            // get associated token
            String pushToken = tokens.get(tickets.indexOf(ticket));

            if (ticket.getStatus() == Status.ERROR) {
                log.error("Ticket response with error, unsubscribing: {}", ticket.getDetails().getError());
            }

            if (ticket.getStatus() == Status.OK) {
                // save ticket details to check for errors later
                PushTicketEntity pushTicketEntity = new PushTicketEntity();
                pushTicketEntity.setId(ticket.getId());

                DeviceEntity device = new DeviceEntity();
                device.setPushToken(pushToken);
                pushTicketEntity.setDeviceEntity(device);

                pushTicketEntity.setNotificationType(notificationType);
                pushTicketEntity.setNotificationTitle(notificationTitle);
                pushTicketEntity.setNotificationBody(notificationBody);
                pushTicketEntity.setLocationId(locationId);
                pushTicketEntity.setWasReceiptChecked(false);
                pushTicketEntity.setTicketCreatedAt(Instant.now());

                pushTicketRepository.save(pushTicketEntity);
            }
        }
    }


}
