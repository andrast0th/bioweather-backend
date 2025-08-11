package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.TranslationEntity;
import com.example.bioweatherbackend.entity.TranslationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRepository extends JpaRepository<TranslationEntity, TranslationId> {
}