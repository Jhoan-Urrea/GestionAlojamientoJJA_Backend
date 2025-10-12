package com.uniquindio.alojamientosAPI.auth;

import com.uniquindio.alojamientosAPI.domain.dto.Auth.AuthResponseDTO;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.LoginDTO;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.RegisterDTO;
import com.uniquindio.alojamientosAPI.domain.service.auth.interfaces.AuthService;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import com.uniquindio.alojamientosAPI.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegisterUser_Success() {
        RegisterDTO registerDTO = new RegisterDTO(
                "Carlos",
                "Gómez",
                LocalDate.of(1992, 6, 10),
                "3005551234",
                "carlos.service@example.com",
                "Password123!",
                "Carrera 50 #30-20"
        );

        AuthResponseDTO response = authService.register(registerDTO);

        assertThat(response).isNotNull();
        assertThat(response.token()).isNotEmpty();
        assertThat(response.userId()).isNotNull();
        assertThat(response.email()).isEqualTo("carlos.service@example.com");
        assertThat(response.firstName()).isEqualTo("Carlos");
        assertThat(response.lastName()).isEqualTo("Gómez");
        assertThat(response.roles()).contains("CLIENTE");

        // Verificar que el usuario fue guardado en la BD
        Optional<UserEntity> savedUser = userRepository.findByEmail("carlos.service@example.com");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getPassword()).isNotEqualTo("Password123!"); // Debe estar encriptada
    }

    @Test
    void testRegisterUser_DuplicateEmail_ThrowsException() {
        RegisterDTO registerDTO = new RegisterDTO(
                "Test",
                "Duplicate",
                LocalDate.of(1990, 1, 1),
                "3001234567",
                "admin@example.com", // Email existente
                "Password123!",
                "Calle 123"
        );

        assertThatThrownBy(() -> authService.register(registerDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El email ya está registrado");
    }

    @Test
    void testLogin_Success() {
        LoginDTO loginDTO = new LoginDTO("admin@example.com", "admin1234");

        AuthResponseDTO response = authService.login(loginDTO);

        assertThat(response).isNotNull();
        assertThat(response.token()).isNotEmpty();
        assertThat(response.userId()).isNotNull();
        assertThat(response.email()).isEqualTo("admin@example.com");
        assertThat(response.roles()).isNotEmpty();
    }

    @Test
    void testLogin_InvalidPassword_ThrowsException() {
        LoginDTO loginDTO = new LoginDTO("admin@example.com", "wrongpassword");

        assertThatThrownBy(() -> authService.login(loginDTO))
                .isInstanceOf(Exception.class);
    }

    @Test
    void testLogin_UserNotFound_ThrowsException() {
        LoginDTO loginDTO = new LoginDTO("noexiste@example.com", "Password123!");

        assertThatThrownBy(() -> authService.login(loginDTO))
                .isInstanceOf(Exception.class);
    }

    @Test
    void testRegisterUser_AssignsClientRoleByDefault() {
        RegisterDTO registerDTO = new RegisterDTO(
                "Maria",
                "López",
                LocalDate.of(1988, 12, 25),
                "3009998888",
                "maria.role@example.com",
                "Password123!",
                "Avenida 10 #20-30"
        );

        AuthResponseDTO response = authService.register(registerDTO);

        assertThat(response.roles()).hasSize(1);
        assertThat(response.roles()).contains("CLIENTE");

        // Verificar en la BD
        Optional<UserEntity> savedUser = userRepository.findByEmail("maria.role@example.com");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getRoles()).hasSize(1);
        assertThat(savedUser.get().getRoles().iterator().next().getName().name()).isEqualTo("CLIENTE");
    }
}
