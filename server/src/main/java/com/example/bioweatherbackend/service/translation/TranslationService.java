package com.example.bioweatherbackend.service.translation;

import com.example.bioweatherbackend.config.cache.CacheConfig;
import com.example.bioweatherbackend.entity.LanguageDto;
import com.example.bioweatherbackend.entity.TranslationDto;
import com.example.bioweatherbackend.entity.TranslationEntity;
import com.example.bioweatherbackend.entity.TranslationId;
import com.example.bioweatherbackend.repository.TranslationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.bioweatherbackend.service.translation.TranslationKeys.TRANSLATION_KEYS;

@Service
@AllArgsConstructor
@Slf4j
public class TranslationService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final TranslationRepository translationRepository;

    public List<LanguageDto> findAllLanguages() {
        List<String> langCodes = translationRepository.findAllLanguages();

        return langCodes.stream().map(code -> {
            var entity = translationRepository.findById(new TranslationId("language.name", code));
            var name = entity.map(TranslationEntity::getText).orElse(code);

            return new LanguageDto(code, name);
        }).toList();
    }

    @Cacheable(CacheConfig.TRANSLATIONS)
    public List<TranslationDto> getTranslations(String language) {
        var translations = translationRepository.findAllByLanguage(language);

        var map = translations.stream().collect(Collectors.toMap(TranslationEntity::getId, TranslationEntity::getText));

        return TRANSLATION_KEYS.stream().map(key -> new TranslationDto(key, map.get(key))).toList();
    }

    @Cacheable(CacheConfig.TRANSLATIONS_MAP)
    public Map<String, String> getTranslationsMap(String language) {
        var translations = translationRepository.findAllByLanguage(language);
        var map = translations.stream().collect(Collectors.toMap(TranslationEntity::getId, TranslationEntity::getText));
        var res = new LinkedHashMap<String, String>();

        TRANSLATION_KEYS.forEach(key -> res.put(key, map.get(key)));

        return res;
    }

    @CacheEvict(value = {CacheConfig.TRANSLATIONS, CacheConfig.TRANSLATIONS_MAP}, allEntries = true)
    @Transactional
    public void deleteLanguage(String language) {
        translationRepository.deleteAllByLanguage(language);
    }

    public String exportTranslationsAsJson(String language) {
        var flatMap = this.getTranslationsMap(language);
        var nestedMap = unflatten(flatMap);
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(nestedMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize translations to JSON", e);
        }
    }

    @CacheEvict(value = {CacheConfig.TRANSLATIONS, CacheConfig.TRANSLATIONS_MAP}, allEntries = true)
    @Transactional
    public void importLanguages(String language, Map<String, Object> json) {
        Map<String, String> flatJson = flatten(json);

        this.translationRepository.deleteAllByLanguage(language);
        var entities = flatJson.entrySet().stream()
            .filter(entry -> TRANSLATION_KEYS.contains(entry.getKey()))
            .map(entry -> new TranslationEntity(entry.getKey(), language, entry.getValue(), Instant.now(), Instant.now()))
            .toList();

        translationRepository.saveAll(entities);
    }

    public static Map<String, String> flatten(Map<String, Object> nested) {
        Map<String, String> flat = new LinkedHashMap<>();
        flattenHelper("", nested, flat);
        return flat;
    }

    @SuppressWarnings("unchecked")
    private static void flattenHelper(String prefix, Map<String, Object> map, Map<String, String> flat) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey():prefix + "." + entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                flattenHelper(key, (Map<String, Object>) value, flat);
            } else {
                flat.put(key, value==null ? null:value.toString());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> unflatten(Map<String, String> flatMap) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : flatMap.entrySet()) {

            String[] parts = entry.getKey().split("\\.");
            Map<String, Object> current = result;

            for (int i = 0; i < parts.length - 1; i++) {
                current = (Map<String, Object>) current.computeIfAbsent(parts[i], k -> new LinkedHashMap<String, Object>());
            }

            current.put(parts[parts.length - 1], entry.getValue());
        }
        return result;
    }

}
