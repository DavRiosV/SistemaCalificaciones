package com.drvservicios.api.controllers;

import com.drvservicios.api.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import com.drvservicios.api.payload.LoginRequest;
import com.drvservicios.api.models.Role;
import com.drvservicios.api.models.User;

import jakarta.servlet.http.HttpServletResponse;
import com.drvservicios.api.security.JwtTokenProvider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint para el inicio de sesión (signin)
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            // Generar el token JWT
            String token = userService.signin(loginRequest.getUsername(), loginRequest.getPassword());

            // Configurar la cookie del token
            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setHttpOnly(true); // Solo accesible por HTTP
            cookie.setSecure(true); // Solo HTTPS en producción
            cookie.setPath("/");
            cookie.setMaxAge((int) (jwtTokenProvider.getExpiration() / 1000));

            // Agregar la cookie a la respuesta
            response.addCookie(cookie);

            return ResponseEntity.ok("Inicio de sesión exitoso");
        } catch (Exception e) {
            logger.error("Error en inicio de sesión: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    // Ruta protegida para verificar el token y permitir el acceso a recursos
    @GetMapping("/home")
    public ResponseEntity<?> home(@CookieValue(name = "JWT_TOKEN", required = false) String token) {
        try {
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }
            return ResponseEntity.ok("Bienvenido al sistema");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    // Endpoint para obtener roles de un usuario por su ID
    @GetMapping("/{id}/roles")
    public List<Role> getRolesByUserId(@PathVariable Long id) {
        return userService.getRolesByUserId(id);
    }

    // Endpoint para validar el token
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@CookieValue(name = "JWT_TOKEN", required = false) String token) {
        try {
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(401).body("Token inválido");
            }
            return ResponseEntity.ok("Token válido");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }
    
    public void registerUser(String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        // Guarda el usuario con la contraseña encriptada en la base de datos
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok("Usuario registrado exitosamente: " + savedUser.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar el usuario.");
        }
    }
}
