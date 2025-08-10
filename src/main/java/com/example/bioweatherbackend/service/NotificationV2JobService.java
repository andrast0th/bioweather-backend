//package com.example.bioweatherbackend.service;
//
//import com.example.bioweatherbackend.repository.NotificationSubscriptionByTypeRepository;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//@AllArgsConstructor
//@Slf4j
//public class NotificationV2JobService {
//
//    private NotificationSubscriptionByTypeRepository repository;
//    private ExpoNotificationService expoNotificationService;
//
//    @Scheduled(cron = "0 0 5,10,15,22 * * *", zone = "UTC")
//    public void scheduledRunWorkJob() {
//        log.info("Running scheduled job to send custom notifications");
//
//        repository.findAll().forEach(notificationSub -> {
//            expoNotificationService.sendTextNotification(Collections.singletonList(notificationSub.getId().getPushToken()),
//                    "Test Custom  " + notificationSub.getId().getNotificationType() + " Notification",
//                    "This is a custom notification for " + notificationSub.getId().getNotificationType() + " type.");
//        });
//
//        log.info("Finished scheduled job to send custom notifications");
//    }
//
//}