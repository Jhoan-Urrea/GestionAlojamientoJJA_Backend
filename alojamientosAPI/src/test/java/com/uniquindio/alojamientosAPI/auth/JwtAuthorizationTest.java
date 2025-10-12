package com.uniquindio.alojamientosAPI.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.AuthResponseDTO;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        // Obtener token del admin para las pruebas
        LoginDTO loginDTO = new LoginDTO("admin@example.com", "admin1234");
        
        MvcResult result = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AuthResponseDTO authResponse = objectMapper.readValue(responseBody, AuthResponseDTO.class);
        adminToken = authResponse.token();
    }

    @Test
    void testAccessPublicEndpoint_WithoutToken() throws Exception {
        // Los endpoints públicos deben ser accesibles sin token
        mockMvc.perform(get("/api/public/test"))
                .andExpect(status().isNotFound()); // 404 porque no existe, pero no 401/403
    }

    @Test
    void testAccessProtectedEndpoint_WithoutToken() throws Exception {
        // Los endpoints protegidos deben rechazar peticiones sin token
        // Spring Security devuelve 403 cuando no hay autenticación
        mockMvc.perform(get("/api/private/test"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessProtectedEndpoint_WithValidToken() throws Exception {
        // Con token válido debe permitir acceso (aunque el endpoint no exista, no debe ser 401)
        mockMvc.perform(get("/api/private/test")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound()); // 404 porque no existe, pero autenticado
    }

    @Test
    void testAccessAdminEndpoint_WithAdminRole() throws Exception {
        // Admin debe poder acceder a endpoints de admin
        mockMvc.perform(get("/api/admin/test")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound()); // 404 porque no existe, pero autorizado
    }

    @Test
    void testAccessProtectedEndpoint_WithInvalidToken() throws Exception {
        // Token inválido debe ser rechazado
        // Spring Security devuelve 403 cuando el token es inválido
        mockMvc.perform(get("/api/private/test")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessProtectedEndpoint_WithExpiredToken() throws Exception {
        // Token expirado debe ser rechazado
        // Spring Security devuelve 403 cuando el token es inválido o expirado
        String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiZXhwIjoxNjAwMDAwMDAwfQ.invalid";
        
        mockMvc.perform(get("/api/private/test")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isForbidden());
    }
}
