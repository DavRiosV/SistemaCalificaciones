package com.drvservicios.api.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthentication extends AbstractAuthenticationToken {

    private final String username;

    // Constructor para username solamente
    public JwtAuthentication(String username) {
        super(null);
        this.username = username;
        setAuthenticated(false); // No autenticado al crearse
    }

    // Constructor para username con roles/authorities
    public JwtAuthentication(String username, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = username;
        setAuthenticated(true); // Autenticado si se proporcionan authorities
    }

    @Override
    public Object getCredentials() {
        return null; // No se utilizan credenciales adicionales
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
}
