package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.entity.NotificationSubscriptionId;
import com.example.bioweatherbackend.mapper.NotificationSubscriptionMapper;
import com.example.bioweatherbackend.model.notifications.SubscriptionDto;
import com.example.bioweatherbackend.repository.NotificationSubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

    private NotificationSubscriptionRepository repo;
    private NotificationSubscriptionMapper mapper;

    public void subscribe(SubscriptionDto subscriptionDto) {

        NotificationSubscriptionId id = new NotificationSubscriptionId(subscriptionDto.getPushToken(), subscriptionDto.getLocationId());

        if(subscriptionDto.getPushToken() != null && repo.findById(id).isEmpty()) {
            repo.save(mapper.toEntity(subscriptionDto));
        }
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



}
