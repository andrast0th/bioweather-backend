package com.example.bioweatherbackend.service;

import com.niamedtech.expo.exposerversdk.ExpoPushNotificationClient;
import com.niamedtech.expo.exposerversdk.request.PushNotification;
import com.niamedtech.expo.exposerversdk.response.TicketResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExpoNotificationsService {

    @Value("${expo.api-key}")
    private String expoApiKey;

    public void sendNotification(String token) {
        List<String> to = new ArrayList<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        to.add("ExponentPushToken[" + token + "]");

        ExpoPushNotificationClient client = ExpoPushNotificationClient
                .builder()
                .setHttpClient(httpClient)
                .setAccessToken(expoApiKey)
                .build();

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTo(to);
        pushNotification.setTitle("Title");
        pushNotification.setBody("Message");

        List<PushNotification> notifications = new ArrayList<>();
        notifications.add(pushNotification);

        List<TicketResponse.Ticket> response;
        try {
            response = client.sendPushNotifications(notifications);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (TicketResponse.Ticket ticket : response) {
            log.info(ticket.getMessage());
        }
    }
}
