package com.example.bioweatherbackend.config.restclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {

    private @Value("${expo.api.url}") String expoApiUrl;
    private @Value("${expo.api.api-key}") String expoApiKey;

    private @Value("${meteonews.api.url}") String meteoNewsApiUrl;
    private @Value("${meteonews.api.api-key}") String meteoNewsApiKey;


    @Bean
    public RestClient expoRestClient() {
        return RestClient.builder()
                .baseUrl(expoApiUrl)
                .defaultHeader("Authorization", "Bearer " + expoApiKey)
                .requestInterceptor(new RestClientLoggingInterceptor())
                .requestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .build();
    }

    @Bean
    public RestClient meteoNewsRestClient() {
        return RestClient.builder()
                .baseUrl(meteoNewsApiUrl)
                .defaultHeader("Authorization", meteoNewsApiKey)
                .requestInterceptor(new RestClientLoggingInterceptor())
                .requestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .messageConverters(converters -> {
                    converters.add(new org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter());
                    converters.add(new org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter());
                })
                .build();
    }

}