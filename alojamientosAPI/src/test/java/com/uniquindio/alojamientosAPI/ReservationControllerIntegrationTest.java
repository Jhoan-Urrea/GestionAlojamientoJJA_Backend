package com.uniquindio.alojamientosAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.alojamientosAPI.domain.dto.ReservationDTO;
import com.uniquindio.alojamientosAPI.web.ReservationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ReservationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private com.uniquindio.alojamientosAPI.domain.services.ReservationService reservationService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearReserva_endpoint_exito() throws Exception {
        ReservationDTO dto = new ReservationDTO();
        dto.setUserId(1L);
        dto.setAccommodationId(2L);
        dto.setStartDate(LocalDate.now());
        dto.setEndDate(LocalDate.now().plusDays(2));
        dto.setGuestIds(List.of(1L, 3L));
        dto.setState("PENDIENTE");
        when(reservationService.createReservation(anyLong(), anyLong(), any(), any(), any())).thenReturn(new com.uniquindio.alojamientosAPI.persistence.entity.Reservation());
        mockMvc.perform(post("/reservations").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void cancelarReserva_endpoint_noAutorizado() throws Exception {
        doThrow(new com.uniquindio.alojamientosAPI.domain.exception.UsuarioNoAutorizadoException("No tienes permiso")).when(reservationService).cancelReservation(anyLong(), anyLong());
        mockMvc.perform(put("/reservations/1/cancel").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":99}"))
                .andExpect(status().isForbidden());
    }
}
