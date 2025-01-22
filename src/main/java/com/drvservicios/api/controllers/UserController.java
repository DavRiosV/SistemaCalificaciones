package com.drvservicios.api.controllers;

import com.drvservicios.api.services.UserService;
import com.drvservicios.api.payload.LoginRequest;
import com.drvservicios.api.models.Role;
import com.drvservicios.api.payload.JwtResponse;
import com.drvservicios.api.security.JwtTokenProvider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/users")
public class UserController {
	
	 private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService; // Inyección del servicio UserService

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Endpoint para el inicio de sesión (signin)
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
        try {
            // Usamos el servicio UserService para autenticar al usuario y obtener el token
            String token = userService.signin(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(new JwtResponse(token));  // Devolver el token JWT
        } catch (Exception e) {
            // Si ocurre un error, devolvemos una respuesta de "Credenciales inválidas"
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    // Ruta protegida para verificar el token y permitir el acceso a recursos
    @GetMapping("/home")
    public ResponseEntity<?> home(@RequestHeader("Authorization") String token) {
        try {
            // Eliminar el prefijo "Bearer " del token si está presente
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Verificar si el token es válido
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }

            // Si el token es válido, redirigir al home
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "http://localhost:8081/home") // Cambia a la ruta de tu frontend
                    .build();
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
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        logger.info("Solicitud de validación de token recibida con encabezado Authorization: {}", token);
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            } else {
                logger.warn("Token no tiene el prefijo 'Bearer'.");
                return ResponseEntity.status(400).body("Formato de token inválido.");
            }

            boolean isValid = jwtTokenProvider.validateToken(token);
            if (isValid) {
                logger.info("Token válido.");
                return ResponseEntity.ok("Token válido");
            } else {
                logger.warn("Token inválido.");
                return ResponseEntity.status(401).body("Token inválido");
            }
        } catch (Exception e) {
            logger.error("Error al validar el token: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

}
