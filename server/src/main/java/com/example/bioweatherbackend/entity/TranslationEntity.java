package com.example.bioweatherbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "translation")
@IdClass(TranslationId.class)
@Getter
@Setter
public class TranslationEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Id
    @Column(name = "language")
    private String language;

    @Column(name = "text", length = 1000)
    private String text;

    @Column(name = "created_timestamp")
    private Instant createdTimestamp;

    @Column(name = "modified_timestamp")
    private Instant updatedTimestamp;

}
