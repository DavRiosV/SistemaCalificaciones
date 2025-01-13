package com.drvservicios.api.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Desactivar CSRF para permitir DELETE
            .authorizeHttpRequests()
            .requestMatchers("/api/**").permitAll() // Permitir acceso a los endpoints de la API
            .anyRequest().authenticated(); // Cualquier otra solicitud requiere autenticación

        return http.build();
    }
}
