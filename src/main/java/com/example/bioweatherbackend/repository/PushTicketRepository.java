package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.PushTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PushTicketRepository extends JpaRepository<PushTicketEntity, String> {
    List<PushTicketEntity> findAllByWasReceiptCheckedIsFalse();
}