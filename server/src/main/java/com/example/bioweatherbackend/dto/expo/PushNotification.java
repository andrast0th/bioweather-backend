package com.example.bioweatherbackend.dto.expo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PushNotification {

    private List<String> to;
    private Map<String, Object> data;
    private String title;
    private String subtitle;
    private String body;
    private String sound;
    private Long ttl;
    private Long expiration;
    private Priority priority;
    private Long badge;
    private String channelId;
    private String categoryId;
    private Boolean _contentAvailable;

    public enum Priority {
        @JsonProperty("default") OK, @JsonProperty("high") ERROR, @JsonProperty("normal") NORMAL
    }

}
