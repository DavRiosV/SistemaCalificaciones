package com.drvservicios.api.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, 
    ROLE_CLIENT,
    ROLE_USER; // Agregado ROLE_USER

    @Override
    public String getAuthority() {
        return name();
    }
}
