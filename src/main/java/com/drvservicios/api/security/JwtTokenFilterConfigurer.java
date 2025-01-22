package com.drvservicios.api.security;

import com.drvservicios.api.utils.JwtUtils;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtUtils jwtUtils;

    public JwtTokenFilterConfigurer(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenValidator jwtTokenValidator = new JwtTokenValidator(jwtUtils);
        http.addFilterBefore(jwtTokenValidator, UsernamePasswordAuthenticationFilter.class);
    }
}
