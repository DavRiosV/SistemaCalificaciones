package com.drvservicios.api.services;

import com.drvservicios.api.models.Role;
import com.drvservicios.api.models.User;
import com.drvservicios.api.reposotories.UserRepository;
import com.drvservicios.api.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);

    private final UserService userService = new UserService(userRepository, passwordEncoder, jwtTokenProvider);

    @Test
    void testSignin_Success() {
        String username = "testUser";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        String token = "jwtToken";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(encodedPassword);
        mockUser.setRoles(Collections.singletonList(Role.ROLE_USER));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.createToken(eq(username), anyList())).thenReturn(token);

        String resultToken = userService.signin(username, password);

        assertNotNull(resultToken);
        assertEquals(token, resultToken);
        verify(userRepository).findByUsername(username);
        verify(passwordEncoder).matches(password, encodedPassword);
        verify(jwtTokenProvider).createToken(eq(username), anyList());
    }

    @Test
    void testSignin_InvalidPassword() {
        String username = "testUser";
        String password = "wrongPassword";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword("encodedPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.signin(username, password));
        assertEquals("Credenciales inválidas", exception.getMessage());
    }

    @Test
    void testSignin_UserNotFound() {
        String username = "unknownUser";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userService.signin(username, password));
        assertEquals("Usuario no encontrado", exception.getMessage());
    }
}
