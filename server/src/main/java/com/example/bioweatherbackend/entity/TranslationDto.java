package com.example.bioweatherbackend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationDto {
    private String id;
    private String language;
    private String text;
}
