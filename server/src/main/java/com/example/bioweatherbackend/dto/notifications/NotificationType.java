package com.example.bioweatherbackend.dto.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {
    BW("bw"),
    BEDTIME("bedtime"),
    WAKEUP("wakeup"),
    LAST_MEAL("last-meal"),
    NEXT_REST("next-rest"),
    PEAK("peak"),
    EXERCISE("exercise"),
    TEST("test");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static NotificationType fromValue(String value) {
        for (NotificationType type : NotificationType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown notification type: " + value);
    }
}