package com.uniquindio.alojamientosAPI.web.reservation;

import com.uniquindio.alojamientosAPI.domain.dto.reservation.ReservationCreateRequest;
import com.uniquindio.alojamientosAPI.domain.dto.reservation.ReservationResponse;
import com.uniquindio.alojamientosAPI.domain.mapper.reservation.ReservationMapper;
import com.uniquindio.alojamientosAPI.domain.service.reservation.ReservationService;
import com.uniquindio.alojamientosAPI.persistence.entity.reservation.ReservationEntity;
import com.uniquindio.alojamientosAPI.security.auth.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody ReservationCreateRequest request
    ) {
        Long userId = extractUserId(principal);
        try {
            ReservationEntity saved = reservationService.createReservation(
                    userId,
                    request.getAccommodationId(),
                    request.getCheckIn(),
                    request.getCheckOut(),
                    request.getCountRoommates()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(ReservationMapper.toResponse(saved));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IllegalStateException e) {
            // Conflicto por disponibilidad u otras reglas de negocio
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<List<ReservationResponse>> myReservations(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long userId = extractUserId(principal);
        List<ReservationResponse> list = reservationService.listByUser(userId)
                .stream()
                .map(ReservationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<Void> cancel(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long reservationId
    ) {
        Long userId = extractUserId(principal);
        try {
            reservationService.cancelReservation(reservationId, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalStateException e) {
            // No autorizado o regla que impide cancelar
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    private Long extractUserId(CustomUserDetails principal) {
        if (principal == null || principal.getUser() == null || principal.getUser().getId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return principal.getUser().getId();
    }
}

