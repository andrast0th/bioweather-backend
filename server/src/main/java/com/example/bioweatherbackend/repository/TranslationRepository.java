package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.TranslationEntity;
import com.example.bioweatherbackend.entity.TranslationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TranslationRepository extends JpaRepository<TranslationEntity, TranslationId> {

    List<TranslationEntity> findAllByLanguage(String language);

    @Query("SELECT DISTINCT t.language FROM TranslationEntity t")
    List<String> findAllLanguages();

    void deleteAllByLanguage(String language);
}