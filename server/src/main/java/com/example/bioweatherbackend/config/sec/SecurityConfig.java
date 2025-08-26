package com.example.bioweatherbackend.config.sec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;

@Configuration
@EnableMethodSecurity  // enables @PreAuthorize on @RequireAuth
public class SecurityConfig {

    Set<Pair<HttpMethod, String>> publicEndpoints = Set.of(
        Pair.of(HttpMethod.GET, "/api/v1/locations/**"),
        Pair.of(HttpMethod.GET, "/api/v1/scales/**"),
        Pair.of(HttpMethod.GET, "/api/v1/astronomy/**"),
        Pair.of(HttpMethod.GET, "/api/v1/weather/**"),
        Pair.of(HttpMethod.GET, "/api/v1/translations/**"),
        Pair.of(HttpMethod.POST, "/api/v1/notifications/subscription"),
        Pair.of(HttpMethod.DELETE, "/api/v1/notifications/subscription")
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> {
                publicEndpoints.forEach(pair ->
                    auth.requestMatchers(pair.getFirst(), pair.getSecond()).permitAll()
                );
                auth.anyRequest().authenticated();
            })
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}