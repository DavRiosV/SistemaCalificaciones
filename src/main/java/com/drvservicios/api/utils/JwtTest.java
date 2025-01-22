package com.drvservicios.api.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtTest {
    public static void main(String[] args) {
        // Clave secreta (debe ser igual a la que usas en tu configuración)
        String secret = "MiClaveSecretaSegura123";

        // Generar el token
        String token = generateToken(secret, "admin", 3600000); // 1 hora de duración

        // Mostrar el token generado
        System.out.println("Token generado: " + token);

        // Validar y decodificar el token
        DecodedJWT decodedJWT = validateToken(secret, token);

        if (decodedJWT != null) {
            System.out.println("Token válido:");
            System.out.println("Sujeto (username): " + decodedJWT.getSubject());
            System.out.println("Roles: " + decodedJWT.getClaim("roles").asList(String.class));
            System.out.println("Emitido en: " + new Date(decodedJWT.getIssuedAt().getTime()));
            System.out.println("Expira en: " + new Date(decodedJWT.getExpiresAt().getTime()));
        } else {
            System.out.println("El token no es válido.");
        }
    }

    // Método para generar un token
    public static String generateToken(String secret, String username, long expiration) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(username)
                .withClaim("roles", java.util.List.of("ROLE_ADMIN")) // Agregar roles
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(algorithm);
    }

    // Método para validar el token
    public static DecodedJWT validateToken(String secret, String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            return JWT.require(algorithm).build().verify(token);
        } catch (Exception e) {
            System.out.println("Error al validar el token: " + e.getMessage());
            return null; // Retorna null si el token no es válido
        }
    }
}
