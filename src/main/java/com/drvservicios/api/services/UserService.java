package com.drvservicios.api.services;

import com.drvservicios.api.models.Role;
import com.drvservicios.api.models.User;
import com.drvservicios.api.reposotories.UserRepository;
import com.drvservicios.api.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // Constructor explícito
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User signup(User user) {
        logger.info("Intentando registrar un nuevo usuario: {}", user.getUsername());
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("El nombre de usuario {} ya existe", user.getUsername());
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encriptar contraseña
        User savedUser = userRepository.save(user);
        logger.info("Usuario registrado exitosamente: {}", savedUser.getUsername());
        return savedUser;
    }

    public String signin(String username, String password) {
        logger.info("Intentando iniciar sesión para el usuario: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado: {}", username);
                    return new RuntimeException("Usuario no encontrado");
                });
        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.error("Credenciales inválidas para el usuario: {}", username);
            throw new RuntimeException("Credenciales inválidas");
        }

        // Generar el token JWT
        List<String> roleNames = user.getRoles().stream()
                                     .map(Role::name)
                                     .toList();
        String token = jwtTokenProvider.createToken(user.getUsername(), roleNames);
        logger.info("Inicio de sesión exitoso para el usuario: {}", username);
        return token;
    }

    public List<Role> getRolesByUserId(Long userId) {
        logger.info("Obteniendo roles del usuario con ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
        return user.getRoles();
    }

    public List<String> getUserRoles(int userId) {
        logger.info("Obteniendo roles en formato de cadena para el usuario con ID: {}", userId);
        User user = userRepository.findById((long) userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
        return user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.toList());
    }

    public User registerUser(User user) {
        logger.info("Registrando un nuevo usuario: {}", user.getUsername());
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.error("El usuario {} ya está registrado.", user.getUsername());
            throw new RuntimeException("El usuario ya existe.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encripta la contraseña
        User savedUser = userRepository.save(user);
        logger.info("Usuario registrado exitosamente: {}", savedUser.getUsername());
        return savedUser;
    }
}
