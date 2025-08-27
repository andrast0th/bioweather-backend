package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.entity.LanguageDto;
import com.example.bioweatherbackend.entity.TranslationDto;
import com.example.bioweatherbackend.service.translation.TranslationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<TranslationDto> getTranslations(@PathVariable("lang") String lang) {
        return service.getTranslations(lang);
    }

    @GetMapping("/{lang}/export")
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
    
}
