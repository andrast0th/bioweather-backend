package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.config.sec.RequireAuth;
import com.example.bioweatherbackend.entity.TranslationDto;
import com.example.bioweatherbackend.service.TranslationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/translations")
@AllArgsConstructor
public class TranslationsController {

    private TranslationService service;

    @GetMapping
    public List<TranslationDto> getSubscriptionsByPushToken() {
        return service.getTranslations();
    }

    @PutMapping
    @RequireAuth
    public void updateTranslations(@RequestBody List<TranslationDto> translations) {
        this.service.updateTranslations(translations);
    }

}
