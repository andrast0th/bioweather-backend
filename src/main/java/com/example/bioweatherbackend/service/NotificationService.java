package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.entity.NotificationSubscriptionId;
import com.example.bioweatherbackend.mapper.NotificationSubscriptionMapper;
import com.example.bioweatherbackend.model.notifications.SubscriptionDto;
import com.example.bioweatherbackend.repository.NotificationSubscriptionRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.niamedtech.expo.exposerversdk.response.TicketResponse.Ticket.Error.DEVICE_NOT_REGISTERED;

@Service
@Slf4j
public class NotificationService {

    private final String expoApiKey;
    private final NotificationSubscriptionRepository repo;
    private final NotificationSubscriptionMapper mapper;

    public NotificationService(
            @Value("${expo.api-key}") String expoApiKey,
            NotificationSubscriptionRepository repo,
            NotificationSubscriptionMapper mapper) {
        this.expoApiKey = expoApiKey;
        this.repo = repo;
        this.mapper = mapper;
    }

    public void subscribe(SubscriptionDto subscriptionDto) {

        NotificationSubscriptionId id = new NotificationSubscriptionId(subscriptionDto.getPushToken(), subscriptionDto.getLocationId());

        if(subscriptionDto.getPushToken() != null && repo.findById(id).isEmpty()) {
            repo.save(mapper.toEntity(subscriptionDto));
        }
    }

    public void unsubscribeByPushToken(String pushToken) {
        repo.deleteById_PushToken(pushToken);
    }

    public void unsubscribe(SubscriptionDto subscriptionDto) {
        NotificationSubscriptionId id = new NotificationSubscriptionId(subscriptionDto.getPushToken(), subscriptionDto.getLocationId());
        if(repo.findById(id).isPresent()) {
            repo.deleteById(id);
        }
    }

    public List<SubscriptionDto> getAllSubscriptions() {
       return repo.findAll().stream().map(entity -> mapper.toDto(entity)).toList();
    }

    public void sendNotification(String title, String subtitle) {
        List<SubscriptionDto> subscriptionDtos = getAllSubscriptions();
        List<String> tokens = subscriptionDtos.stream().map(SubscriptionDto::getPushToken).toList();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        ExpoPushNotificationClient client = ExpoPushNotificationClient
                .builder()
                .setHttpClient(httpClient)
                .setAccessToken(expoApiKey)
                .build();

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTo(tokens);
        pushNotification.setTitle(title);
        pushNotification.setSubtitle(subtitle);
        pushNotification.setBody(subtitle);

        List<PushNotification> notifications = new ArrayList<>();
        notifications.add(pushNotification);

        try {
            List<TicketResponse.Ticket> tickets = client.sendPushNotifications(notifications);
            handleTickets(tickets);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDataOnlyNotification() {
        List<SubscriptionDto> subscriptionDtos = getAllSubscriptions();
        List<String> tokens = subscriptionDtos.stream().map(SubscriptionDto::getPushToken).toList();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        ExpoPushNotificationClient client = ExpoPushNotificationClient
                .builder()
                .setHttpClient(httpClient)
                .setAccessToken(expoApiKey)
                .build();

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTo(tokens);
        pushNotification.setData(Collections.singletonMap("taskName", "updateData"));

        List<PushNotification> notifications = new ArrayList<>();
        notifications.add(pushNotification);

        try {
            List<TicketResponse.Ticket> tickets = client.sendPushNotifications(notifications);
            handleTickets(tickets);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleTickets(List<TicketResponse.Ticket> tickets) {
        tickets.forEach(ticket -> Optional.ofNullable(ticket)
                .map(TicketResponse.Ticket::getDetails)
                .map(TicketResponse.Ticket.Details::getError)
                .filter(err -> err == DEVICE_NOT_REGISTERED)
                .ifPresent(err -> {
                    unsubscribeByPushToken(ticket.getDetails().getExpoPushToken());
                    log.error("Push token {} is not registered, removing from database", ticket.getDetails().getExpoPushToken());
                }));
    }

}
