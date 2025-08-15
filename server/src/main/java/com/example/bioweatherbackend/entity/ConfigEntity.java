package com.example.bioweatherbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "config")
@Getter
@Setter
public class ConfigEntity {
    @Id
    @Column(name = "id", nullable = false, length = 200)
    private String id;

    @Column(name = "value", nullable = false, length = 1000)
    private String value;

    @Column(name = "description", length = 1000)
    private String description;
}

