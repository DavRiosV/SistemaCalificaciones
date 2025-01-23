package com.drvservicios.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.drvservicios.api.security.JwtTokenProvider;
import com.drvservicios.api.security.JwtTokenValidator;
import com.drvservicios.api.services.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/auth/login", "/auth/logout", "/api/users/signin", "/api/users/validate-token").permitAll()
            .requestMatchers("/resources/**", "/static/**", "/public/**").permitAll() // Recursos públicos
            .anyRequest().authenticated()
            .and()
            .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login")
                .deleteCookies("JWT_TOKEN")
                .invalidateHttpSession(true)
                .permitAll()
            .and()
            .addFilterBefore(new JwtTokenValidator(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsServiceImpl;
    }
}
