package com.tokentrackr.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Health and info endpoints
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        // Public user endpoints if any
                        .requestMatchers("/users").permitAll()
                        // Swagger/OpenAPI UI
                        .requestMatchers(
                                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/docs/**"
                        ).permitAll()
                        // Application endpoints
//                        .requestMatchers("/users").authenticated()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .oauth2Login(Customizer.withDefaults())           // optional: remove if no login redirects
                .oauth2ResourceServer(rs -> rs.jwt(Customizer.withDefaults()));
        return http.build();
    }
}



