//package com.example.bioweatherbackend.service;
//
//import com.example.bioweatherbackend.dto.expo.*;
//import com.example.bioweatherbackend.dto.notifications.NotificationSubscriptionDto;
//import com.example.bioweatherbackend.dto.notifications.SubscriptionDto;
//import com.example.bioweatherbackend.entity.NotificationSubscriptionEntity;
//import com.example.bioweatherbackend.entity.PushTicketEntity;
//import com.example.bioweatherbackend.mapper.NotificationSubscriptionMapper;
//import com.example.bioweatherbackend.repository.NotificationSubscriptionRepository;
//import com.example.bioweatherbackend.repository.PushTicketRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestClient;
//
//import java.time.Instant;
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//
//@Service
//@Slf4j
//public class ExpoNotificationService {
//
//    private final RestClient restClient;
//    private final NotificationSubscriptionRepository subscriptionRepository;
//    private final PushTicketRepository pushTicketRepository;
//    private final NotificationSubscriptionMapper mapper;
//
//    public ExpoNotificationService(@Qualifier("expoRestClient") RestClient restClient, NotificationSubscriptionRepository subscriptionRepository, PushTicketRepository pushTicketRepository, NotificationSubscriptionMapper mapper) {
//        this.restClient = restClient;
//        this.subscriptionRepository = subscriptionRepository;
//        this.pushTicketRepository = pushTicketRepository;
//        this.mapper = mapper;
//    }
//
//    public void sendTextNotification(String title, String subtitle) {
//        sendTextNotification(getAllPushTokens(), title, subtitle);
//    }
//
//    public void sendTextNotification(List<String> pushTokens, String title, String subtitle) {
//        PushNotification notification = new PushNotification();
//        notification.setTo(pushTokens);
//        notification.setTitle(title);
//        notification.setSubtitle(subtitle);
//        notification.setBody(subtitle);
//        notification.setSound("default");
//
//        TicketResponse response = restClient.post()
//                .uri("/push/send")
//                .contentType(APPLICATION_JSON)
//                .body(notification)
//                .retrieve()
//                .body(TicketResponse.class);
//
//        if (response != null) {
//            handlePushTicketResponse(pushTokens, response.getData());
//        }
//    }
//
//    public void sendScheduleWorkNotifications() {
//        sendScheduleWorkNotifications(null);
//    }
//
//    public void sendScheduleWorkNotifications(List<String> pushTokens) {
//        PushNotification notification = new PushNotification();
//
//        if (pushTokens == null || pushTokens.isEmpty()) {
//            log.warn("No push tokens provided, using all available tokens.");
//            pushTokens = getAllPushTokens();
//        }
//        notification.setTo(pushTokens);
//
//        notification.set_contentAvailable(true);
//        notification.setData(Collections.singletonMap("taskName", "scheduleWork"));
//
//        TicketResponse response = restClient.post()
//                .uri("/push/send")
//                .contentType(APPLICATION_JSON)
//                .body(notification)
//                .retrieve()
//                .body(TicketResponse.class);
//
//        if (response != null) {
//            handlePushTicketResponse(pushTokens, response.getData());
//        }
//    }
//
//    private void handlePushTicketResponse(List<String> tokens, List<TicketResponse.Ticket> tickets) {
//        if (tickets == null || tickets.isEmpty()) {
//            log.warn("No tickets received from Expo server.");
//            return;
//        }
//
//        for (TicketResponse.Ticket ticket : tickets) {
//
//            // get associated token
//            String pushToken = tokens.get(tickets.indexOf(ticket));
//
//            if (ticket.getStatus() == Status.ERROR) {
//                log.error("Ticket response with error, unsubscribing: {}", ticket.getDetails().getError());
//                disablePushToken(pushToken);
//            }
//
//            if (ticket.getStatus() == Status.OK) {
//                // save ticket details to check for errors later
//                PushTicketEntity pushTicketEntity = new PushTicketEntity();
//                pushTicketEntity.setId(ticket.getId());
//                NotificationSubscriptionEntity subscriptionEntity = new NotificationSubscriptionEntity();
//                subscriptionEntity.setPushToken(pushToken);
//                pushTicketEntity.setNotificationSubscription(subscriptionEntity);
//                pushTicketEntity.setWasReceiptChecked(false);
//                pushTicketEntity.setTicketCreatedAt(Instant.now());
//                pushTicketRepository.save(pushTicketEntity);
//            }
//        }
//    }
//
//    public void subscribe(SubscriptionDto subscriptionDto) {
//        var entity = mapper.toEntity(subscriptionDto);
//        entity.setUpdatedViaSubscribe(Instant.now());
//        entity.setDisabled(false);
//        subscriptionRepository.save(entity);
//    }
//
//    public void unsubscribe(SubscriptionDto subscriptionDto) {
//        Optional.ofNullable(subscriptionDto)
//                .map(SubscriptionDto::getPushToken)
//                .ifPresent(this::disablePushToken);
//    }
//
//    public void disablePushToken(String pushToken) {
//        subscriptionRepository.updateIsDisabledByPushToken(pushToken, true);
//    }
//
//    public List<String> getAllPushTokens() {
//        return subscriptionRepository.findByIsDisabled(false)
//                .stream()
//                .map(NotificationSubscriptionEntity::getPushToken)
//                .toList();
//    }
//
//    public List<NotificationSubscriptionDto> getAllNotificationSubscriptions() {
//        return subscriptionRepository.findAll()
//                .stream()
//                .map(mapper::toDto)
//                .toList();
//    }
//
//    public void checkPendingPushTicketReceipts() {
//        checkPendingPushTicketReceipts(null);
//    }
//
//    public void checkPendingPushTicketReceipts(List<String> pushTokens) {
//
//        List<PushTicketEntity> pushTickets;
//        if (pushTokens == null || pushTokens.isEmpty()) {
//            pushTickets = pushTicketRepository.findAllByWasReceiptCheckedIsFalse();
//        } else {
//            pushTickets = pushTicketRepository.findAllNotCheckedByPushTokens(pushTokens);
//        }
//
//        if(pushTickets.isEmpty()) {
//            log.info("No push tickets to check receipts for.");
//            return;
//        }
//
//        ReceiptRequest request = new ReceiptRequest(pushTickets.stream().map(PushTicketEntity::getId).toList());
//
//        ReceiptResponse receiptResponse = restClient.post()
//                .uri("/push/getReceipts")
//                .body(request)
//                .retrieve()
//                .body(ReceiptResponse.class);
//
//        if (receiptResponse != null) {
//            receiptResponse.getData().forEach((ticketId, receipt) -> {
//                PushTicketEntity ticket = pushTicketRepository.findById(ticketId).orElse(null);
//                if (ticket != null) {
//                    ticket.setWasReceiptChecked(true);
//                    ticket.setReceiptCheckedAt(Instant.now());
//                    ticket.setReceiptStatus(receipt.getStatus().toString());
//
//                    Optional.of(receipt)
//                            .map(ReceiptResponse.Receipt::getDetails)
//                            .map(ReceiptResponse.Receipt.Details::getError)
//                            .ifPresent(receiptError -> {
//                                log.error("Received error response for saving push ticket: {}, error: {}, message: {}", ticket.getId(), receiptError, receipt.getMessage());
//                                ticket.setReceiptError(receiptError.toString());
//                            });
//
//                    pushTicketRepository.save(ticket);
//                } else {
//                    log.warn("No push ticket found for ID: {}", ticketId);
//                }
//            });
//        }
//    }
//
//    public void cleanupDevicesWithPushReceiptErrors() {
//        List<String> tokensWithErrors = pushTicketRepository.findPushTokensWithErrorsSince(2, Instant.now().minusSeconds(60 * 60 * 24));
//        if (tokensWithErrors.isEmpty()) {
//            log.info("No devices with push receipt errors found.");
//            return;
//        }
//        tokensWithErrors.forEach(token -> {
//            List<PushTicketEntity> tickets = pushTicketRepository.findAllByPushToken(token);
//            tickets.stream().findFirst().ifPresent(pushTicket -> {
//                if(Objects.equals(pushTicket.getReceiptStatus(), "ERROR")) {
//                    log.warn("Unsubscribing device with push token due to receipt errors: {}", token);
//                    disablePushToken(token);
//                }
//            });
//        });
//    }
//
//}
