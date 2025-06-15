package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.NotificationSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscriptionEntity, String> {

    // update disabled by push token
    @Modifying
    @Transactional
    @Query("UPDATE NotificationSubscriptionEntity n SET n.isDisabled = :isDisabled WHERE n.pushToken = :pushToken")
    void updateIsDisabledByPushToken(@Param("pushToken") String pushToken, @Param("isDisabled") boolean isDisabled);

    @Query("SELECT n FROM NotificationSubscriptionEntity n WHERE n.isDisabled = :isDisabled")
    List<NotificationSubscriptionEntity> findByIsDisabled(boolean isDisabled);

}