package com.example.bioweatherbackend.entity;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranslationId implements Serializable {
    private String id;
    private String language;
}