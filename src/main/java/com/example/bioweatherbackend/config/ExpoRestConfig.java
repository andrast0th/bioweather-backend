package com.example.bioweatherbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ExpoRestConfig {

    private @Value("${expo.api.url}") String expoApiUrl;
    private @Value("${expo.api.api-key}") String expoApiKey;

    @Bean
    public RestClient expoRestClient() {
        return RestClient.builder()
                .baseUrl(expoApiUrl)
                .defaultHeader("Authorization", "Bearer " + expoApiKey)
                .requestInterceptor(new ExpoRestClientLoggingInterceptor())
                .requestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .build();
    }

}