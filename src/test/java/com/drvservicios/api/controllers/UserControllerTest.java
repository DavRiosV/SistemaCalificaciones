package com.drvservicios.api.controllers;

import com.drvservicios.api.services.UserService;
import com.drvservicios.api.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void testGetRolesWithValidToken() throws Exception {
        Mockito.when(userService.getUserRoles(3))
                .thenReturn(List.of("ROLE_ADMIN"));

        Mockito.when(jwtTokenProvider.validateToken(Mockito.anyString()))
                .thenReturn(true);

        Mockito.when(jwtTokenProvider.getUsername(Mockito.anyString()))
                .thenReturn("testUser");

        mockMvc.perform(get("/api/users/3/roles")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("ROLE_ADMIN"));
    }

    @Test
    void testGetRolesWithInvalidToken() throws Exception {
        Mockito.when(jwtTokenProvider.validateToken(Mockito.anyString()))
                .thenReturn(false);

        mockMvc.perform(get("/api/users/3/roles")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetRolesWithoutToken() throws Exception {
        mockMvc.perform(get("/api/users/3/roles"))
                .andExpect(status().isForbidden());
    }
}
