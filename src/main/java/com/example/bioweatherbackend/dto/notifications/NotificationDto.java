package com.example.bioweatherbackend.dto.notifications;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class NotificationDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String subtitle;
}
