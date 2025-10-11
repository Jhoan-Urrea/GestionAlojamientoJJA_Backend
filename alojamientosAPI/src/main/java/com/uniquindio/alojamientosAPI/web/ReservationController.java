package com.uniquindio.alojamientosAPI.web;

import com.uniquindio.alojamientosAPI.domain.dto.ReservationDTO;
import com.uniquindio.alojamientosAPI.domain.mapper.ReservationMapper;
import com.uniquindio.alojamientosAPI.domain.services.ReservationService;
import com.uniquindio.alojamientosAPI.persistence.entity.Reservation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ReservationDTO createReservation(@RequestBody ReservationRequest request) {
        Reservation reserva = reservationService.createReservation(
            request.getUserId(),
            request.getAccommodationId(),
            request.getStartDate(),
            request.getEndDate(),
            request.getGuestIds()
        );
        return ReservationMapper.toDTO(reserva);
    }

    @GetMapping("/user/{userId}")
    public List<ReservationDTO> getReservationsByUser(@PathVariable Long userId, @RequestParam Long requesterId) {
        // Solo el propietario puede ver sus reservas
        if (!userId.equals(requesterId)) {
            throw new RuntimeException("No autorizado para ver estas reservas");
        }
        return reservationService.getReservationsByUser(userId)
                .stream()
                .map(ReservationMapper::toDTO)
                .toList();
    }

    @PutMapping("/{id}/cancel")
    public boolean cancelReservation(@PathVariable Long id, @RequestBody CancelRequest request) {
        // Solo el propietario puede cancelar
        return reservationService.cancelReservation(id, request.getUserId());
    }

    // DTO para la petición de reserva
    @Setter
    @Getter
    public static class ReservationRequest {
        // Getters y setters
        private Long userId;
        private Long accommodationId;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<Long> guestIds;

    }
    @Setter
    @Getter
    public static class CancelRequest {
        private Long userId;

    }
}
