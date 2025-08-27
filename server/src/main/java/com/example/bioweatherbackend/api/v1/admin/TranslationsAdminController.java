package com.example.bioweatherbackend.api.v1.admin;

import com.example.bioweatherbackend.service.translation.TranslationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/translations")
@AllArgsConstructor
public class TranslationsAdminController {

    private TranslationService service;

    @DeleteMapping("/{lang}")
    public void deleteLanguage(@PathVariable("lang") String lang) {
        if (lang.equals("en")) {
            throw new IllegalArgumentException("Cannot delete default language!");
        }

        service.deleteLanguage(lang);
    }

    @PostMapping("/{lang}/import")
    public ResponseEntity<Void> importLang(@PathVariable("lang") String lang, @RequestBody Map<String, Object> json) {
        service.importLanguages(lang, json);
        return ResponseEntity.accepted().build();
    }
    
}

