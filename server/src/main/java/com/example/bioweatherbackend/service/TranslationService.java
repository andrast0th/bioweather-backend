package com.example.bioweatherbackend.service;

import com.example.bioweatherbackend.entity.TranslationDto;
import com.example.bioweatherbackend.entity.TranslationEntity;
import com.example.bioweatherbackend.mapper.DashboardMapper;
import com.example.bioweatherbackend.repository.TranslationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final DashboardMapper mapper;

    public List<TranslationDto> getTranslations() {
        return mapper.toTranslationDtoList(translationRepository.findAll());
    }

    @Transactional
    public void updateTranslations(List<TranslationDto> translationDto) {
        translationDto.forEach(dto -> {
            TranslationEntity entity = mapper.toTranslationEntity(dto);
            entity.setUpdatedTimestamp(Instant.now());
            translationRepository.save(entity);
        });
    }


}
