package com.uniquindio.alojamientosAPI.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.alojamientosAPI.domain.services.ReviewService;
import com.uniquindio.alojamientosAPI.persistence.entity.GuestRating;
import com.uniquindio.alojamientosAPI.persistence.entity.ReviewComment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReviewService reviewService;

    @Test
    void rateReservation_success() throws Exception {
        ReviewController.RateRequest request = new ReviewController.RateRequest();
        request.setGuestId(1L);
        request.setRating(5);
        when(reviewService.rateReservation(10L, 1L, 5)).thenReturn(new GuestRating());
        mockMvc.perform(post("/reservations/10/rate").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void rateReservation_conflict() throws Exception {
        ReviewController.RateRequest request = new ReviewController.RateRequest();
        request.setGuestId(1L);
        request.setRating(5);
        doThrow(new IllegalStateException("La reserva ya tiene una calificación registrada"))
                .when(reviewService).rateReservation(10L, 1L, 5);
        mockMvc.perform(post("/reservations/10/rate").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().string("La reserva ya tiene una calificación registrada"));
    }

    @Test
    void commentReservation_success() throws Exception {
        ReviewController.CommentRequest request = new ReviewController.CommentRequest();
        request.setGuestId(1L);
        request.setComment("Muy bueno");
        when(reviewService.commentReservation(10L, 1L, "Muy bueno")).thenReturn(new ReviewComment());
        mockMvc.perform(post("/reservations/10/comment").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void commentReservation_conflict() throws Exception {
        ReviewController.CommentRequest request = new ReviewController.CommentRequest();
        request.setGuestId(1L);
        request.setComment("Muy bueno");
        doThrow(new IllegalStateException("La reserva ya tiene un comentario registrado"))
                .when(reviewService).commentReservation(10L, 1L, "Muy bueno");
        mockMvc.perform(post("/reservations/10/comment").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().string("La reserva ya tiene un comentario registrado"));
    }

    @Test
    void rateReservation_invalidRating() throws Exception {
        ReviewController.RateRequest request = new ReviewController.RateRequest();
        request.setGuestId(1L);
        request.setRating(0); // rating inválido
        mockMvc.perform(post("/reservations/10/rate").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
