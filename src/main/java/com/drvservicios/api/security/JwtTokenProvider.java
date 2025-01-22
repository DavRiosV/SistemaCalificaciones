package com.drvservicios.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        logger.info("Inicializando JwtTokenProvider");
        if (secret.length() < 32) {
            logger.error("La clave secreta es demasiado corta. Debe tener al menos 32 caracteres.");
            throw new IllegalArgumentException("La clave secreta debe tener al menos 256 bits (32 caracteres).");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        logger.info("Clave secreta inicializada correctamente");
    }

    public String createToken(String username, List<String> roles) {
        logger.info("Creando token JWT para el usuario: {}", username);
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + expiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        logger.info("Token JWT creado exitosamente para el usuario: {}", username);
        return token;
    }

    public String getUsername(String token) {
        logger.info("Extrayendo el nombre de usuario del token");
        try {
            String username = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            logger.info("Nombre de usuario extraído exitosamente: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("Error al extraer el nombre de usuario del token: {}", e.getMessage());
            throw new RuntimeException("Token inválido");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);  // Intenta analizar el token
            return true;
        } catch (Exception e) {
            return false;  // Si hay un error, el token no es válido
        }
    }


}
