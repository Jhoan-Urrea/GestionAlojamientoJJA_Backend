package com.uniquindio.alojamientosAPI.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.LoginDTO;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.RegisterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterUser_Success() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO(
                "Juan",
                "Pérez",
                LocalDate.of(1990, 1, 15),
                "3001234567",
                "juan.test@example.com",
                "Password123!",
                "Calle 123 #45-67"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.email").value("juan.test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Pérez"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[0]").value("CLIENTE"));
    }

    @Test
    void testRegisterUser_DuplicateEmail() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO(
                "Admin",
                "Test",
                LocalDate.of(1985, 5, 20),
                "3009876543",
                "admin@example.com", // Email que ya existe (del DataInitializer)
                "Password123!",
                "Calle 999"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void testRegisterUser_InvalidPassword() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO(
                "Test",
                "User",
                LocalDate.of(1995, 3, 10),
                "3001111111",
                "test.invalid@example.com",
                "weak", // Contraseña débil
                "Calle 456"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginDTO loginDTO = new LoginDTO(
                "admin@example.com",
                "admin1234"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.roles").isArray());
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO(
                "admin@example.com",
                "wrongpassword"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogin_UserNotFound() throws Exception {
        LoginDTO loginDTO = new LoginDTO(
                "noexiste@example.com",
                "Password123!"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }
}
