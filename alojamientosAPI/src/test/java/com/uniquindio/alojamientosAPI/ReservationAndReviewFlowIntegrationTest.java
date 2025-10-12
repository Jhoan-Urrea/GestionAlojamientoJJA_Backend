package com.uniquindio.alojamientosAPI;

import com.uniquindio.alojamientosAPI.domain.exception.ReservaSuperpuestaException;
import com.uniquindio.alojamientosAPI.domain.exception.UsuarioNoAutorizadoException;
import com.uniquindio.alojamientosAPI.domain.services.ReservationService;
import com.uniquindio.alojamientosAPI.domain.services.ReviewService;
import com.uniquindio.alojamientosAPI.persistence.entity.Accommodation;
import com.uniquindio.alojamientosAPI.persistence.entity.Reservation;
import com.uniquindio.alojamientosAPI.persistence.entity.StateReservation;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import com.uniquindio.alojamientosAPI.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ReservationAndReviewFlowIntegrationTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;
    @Autowired
    private StateReservationRepository stateReservationRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    private StateReservation estadoPendiente;
    private StateReservation estadoConfirmada;
    private StateReservation estadoFinalizada;

    @BeforeEach
    void setupStates() {
        // Sembrar estados requeridos para los flujos de prueba
        estadoPendiente = ensureState("PENDIENTE");
        estadoConfirmada = ensureState("CONFIRMADA");
        estadoFinalizada = ensureState("FINALIZADA");
        ensureState("CANCELADA");
    }

    private StateReservation ensureState(String name) {
        return stateReservationRepository.findAll().stream()
                .filter(s -> name.equalsIgnoreCase(s.getName()))
                .findFirst()
                .orElseGet(() -> {
                    StateReservation s = new StateReservation();
                    s.setName(name);
                    return stateReservationRepository.save(s);
                });
    }

    private UserEntity newUser(String email) {
        UserEntity u = new UserEntity();
        u.setFirstName("Test");
        u.setLastName("User");
        u.setEmail(email);
        u.setPassword("pwd");
        return userRepository.save(u);
    }

    private Accommodation newAccommodation(String nombre) {
        Accommodation a = new Accommodation();
        a.setNombre(nombre);
        a.setDireccion("Dir " + nombre);
        return accommodationRepository.save(a);
    }

    @Test
    void flujoCompleto_reservaFinalizada_calificaYComenta_exito() {
        UserEntity owner = newUser("owner1@example.com");
        UserEntity guest1 = newUser("guest1@example.com");
        Accommodation acc = newAccommodation("Casa 1");

        // Crear reserva PENDIENTE
        Reservation r = reservationService.createReservation(
                owner.getId(), acc.getId(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), List.of(guest1.getId()));
        assertNotNull(r.getId());
        assertEquals("PENDIENTE", r.getState().getName());

        // Marcar como FINALIZADA
        r.setState(estadoFinalizada);
        reservationRepository.save(r);

        // Calificar
        var rating = reviewService.rateReservation(r.getId(), owner.getId(), 5);
        assertNotNull(rating);
        assertEquals(5, rating.getRating());
        assertEquals(r.getId(), rating.getReservation().getId());

        // Comentar
        var comment = reviewService.commentReservation(r.getId(), owner.getId(), "Excelente");
        assertNotNull(comment);
        assertEquals("Excelente", comment.getComment());
        assertEquals(r.getId(), comment.getReservation().getId());

        // Verificar asociaciones en la reserva
        Reservation rReload = reservationRepository.findById(r.getId()).orElseThrow();
        assertNotNull(rReload.getGuestRating());
        assertNotNull(rReload.getReviewComment());
    }

    @Test
    void calificar_reservaNoFinalizada_lanzaIllegalArgument() {
        UserEntity owner = newUser("owner2@example.com");
        Accommodation acc = newAccommodation("Casa 2");

        Reservation r = reservationService.createReservation(
                owner.getId(), acc.getId(), LocalDate.now().plusDays(2), LocalDate.now().plusDays(4), Collections.emptyList());
        assertEquals("PENDIENTE", r.getState().getName());

        assertThrows(IllegalArgumentException.class, () ->
                reviewService.rateReservation(r.getId(), owner.getId(), 4));
    }

    @Test
    void calificar_usuarioNoPropietario_lanza403() {
        UserEntity owner = newUser("owner3@example.com");
        UserEntity other = newUser("other3@example.com");
        Accommodation acc = newAccommodation("Casa 3");

        Reservation r = reservationService.createReservation(
                owner.getId(), acc.getId(), LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), Collections.emptyList());
        r.setState(estadoFinalizada);
        reservationRepository.save(r);

        assertThrows(UsuarioNoAutorizadoException.class, () ->
                reviewService.rateReservation(r.getId(), other.getId(), 5));
    }

    @Test
    void calificar_duplicado_lanzaConflict() {
        UserEntity owner = newUser("owner4@example.com");
        Accommodation acc = newAccommodation("Casa 4");

        Reservation r = reservationService.createReservation(
                owner.getId(), acc.getId(), LocalDate.now().plusDays(4), LocalDate.now().plusDays(6), Collections.emptyList());
        r.setState(estadoFinalizada);
        reservationRepository.save(r);

        // Primera calificación OK
        reviewService.rateReservation(r.getId(), owner.getId(), 5);
        // Segunda debe fallar
        assertThrows(IllegalStateException.class, () ->
                reviewService.rateReservation(r.getId(), owner.getId(), 4));
    }

    @Test
    void crearReserva_superpuestaConConfirmada_lanzaExcepcion() {
        UserEntity owner = newUser("owner5@example.com");
        Accommodation acc = newAccommodation("Casa 5");

        // Reserva A y marcar como CONFIRMADA
        Reservation r1 = reservationService.createReservation(
                owner.getId(), acc.getId(), LocalDate.now().plusDays(5), LocalDate.now().plusDays(7), Collections.emptyList());
        r1.setState(estadoConfirmada);
        reservationRepository.save(r1);

        // Intentar crear otra reserva solapada en mismas fechas
        assertThrows(ReservaSuperpuestaException.class, () ->
                reservationService.createReservation(
                        owner.getId(), acc.getId(), LocalDate.now().plusDays(6), LocalDate.now().plusDays(8), Collections.emptyList()));
    }
}

