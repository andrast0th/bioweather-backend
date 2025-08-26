package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.config.sec.RequireAuth;
import com.example.bioweatherbackend.entity.LanguageDto;
import com.example.bioweatherbackend.entity.TranslationDto;
import com.example.bioweatherbackend.service.translation.TranslationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/translations")
@AllArgsConstructor
public class TranslationsController {

    private TranslationService service;

    @GetMapping()
    public List<LanguageDto> findAllLanguages() {
        return service.findAllLanguages();
    }

    @GetMapping("/{lang}")
    public List<TranslationDto> getSubscriptionsByPushToken(@PathVariable("lang") String lang) {
        return service.getTranslations(lang);
    }

    @DeleteMapping("/{lang}")
    @RequireAuth
    public void deleteLanguage(@PathVariable("lang") String lang) {
        if (lang.equals("en")) {
            throw new IllegalArgumentException("Cannot delete default language!");
        }

        service.deleteLanguage(lang);
    }

    @GetMapping("/{lang}/export")
    @RequireAuth
    public ResponseEntity<byte[]> exportLang(@PathVariable("lang") String lang) {
        String properties = service.exportTranslationsAsJson(lang);

        byte[] content = properties.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", lang + ".json");

        return ResponseEntity.ok()
            .headers(headers)
            .body(content);
    }

    @PostMapping("/{lang}/import")
    @RequireAuth
    public ResponseEntity<Void> importLang(@PathVariable("lang") String lang, @RequestBody Map<String, Object> json) {
        service.importLanguages(lang, json);
        return ResponseEntity.accepted().build();
    }


}
