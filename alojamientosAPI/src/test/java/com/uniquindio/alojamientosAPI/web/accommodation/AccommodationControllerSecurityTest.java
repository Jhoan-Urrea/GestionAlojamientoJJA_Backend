package com.uniquindio.alojamientosAPI.web.accommodation;

import com.uniquindio.alojamientosAPI.domain.dto.accommodation.AccommodationDTO;
import com.uniquindio.alojamientosAPI.domain.mapper.accommodation.AccommodationMapper;
import com.uniquindio.alojamientosAPI.domain.service.accommodation.AccommodationService;
import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.AccommodationEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccommodationController.class)
@Import({AccommodationControllerSecurityTest.SecurityTestConfig.class, AccommodationControllerSecurityTest.MockBeansConfig.class})
class AccommodationControllerSecurityTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccommodationService accommodationService;

    @Autowired
    AccommodationMapper accommodationMapper;

    @TestConfiguration
    static class MockBeansConfig {
        @Bean
        AccommodationService accommodationService() {
            return Mockito.mock(AccommodationService.class);
        }
        @Bean
        AccommodationMapper accommodationMapper() {
            return Mockito.mock(AccommodationMapper.class);
        }
    }

    @Configuration
    @EnableMethodSecurity
    static class SecurityTestConfig {
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Test
    @DisplayName("POST /api/accommodations con CLIENTE -> 403")
    @WithMockUser(roles = {"CLIENTE"})
    void create_withCliente_forbidden() throws Exception {
        mockMvc.perform(post("/api/accommodations")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/accommodations con ANFITRION -> 200")
    @WithMockUser(roles = {"ANFITRION"})
    void create_withHost_ok() throws Exception {
        AccommodationEntity entity = new AccommodationEntity();
        AccommodationDTO dto = AccommodationDTO.builder().id(1).title("T").build();
        Mockito.when(accommodationMapper.toEntity(any())).thenReturn(entity);
        Mockito.when(accommodationService.create(any())).thenReturn(entity);
        Mockito.when(accommodationMapper.toDto(any())).thenReturn(dto);

        mockMvc.perform(post("/api/accommodations")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"T\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/accommodations/{id} con CLIENTE -> 403")
    @WithMockUser(roles = {"CLIENTE"})
    void update_withCliente_forbidden() throws Exception {
        mockMvc.perform(put("/api/accommodations/1")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /api/accommodations/{id} con CLIENTE -> 403")
    @WithMockUser(roles = {"CLIENTE"})
    void delete_withCliente_forbidden() throws Exception {
        mockMvc.perform(delete("/api/accommodations/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/accommodations/{id}/activate con ADMINISTRADOR -> 200")
    @WithMockUser(roles = {"ADMINISTRADOR"})
    void activate_withAdmin_ok() throws Exception {
        AccommodationEntity entity = new AccommodationEntity();
        AccommodationDTO dto = AccommodationDTO.builder().id(1).title("T").build();
        Mockito.when(accommodationService.activate(1L)).thenReturn(entity);
        Mockito.when(accommodationMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(post("/api/accommodations/1/activate"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/accommodations/{id}/activate con CLIENTE -> 403")
    @WithMockUser(roles = {"CLIENTE"})
    void activate_withCliente_forbidden() throws Exception {
        mockMvc.perform(post("/api/accommodations/1/activate"))
                .andExpect(status().isForbidden());
    }
}
