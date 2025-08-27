package com.example.bioweatherbackend.config.sec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String[] AUTHENTICATED_ENDPOINTS = {
        "/actuator/**",
        "/api/v1/admin/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
                .anyRequest().permitAll())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}