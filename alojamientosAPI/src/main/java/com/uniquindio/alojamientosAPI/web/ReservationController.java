package com.uniquindio.alojamientosAPI.web;

import com.uniquindio.alojamientosAPI.domain.services.ReservationService;
import com.uniquindio.alojamientosAPI.persistence.entity.Reservation;
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
    public Reservation createReservation(@RequestBody ReservationRequest request) {
        // Aquí deberías obtener el usuario y alojamiento por ID usando sus repositorios
        // Ejemplo: User user = userRepository.findById(request.getUserId()).orElse(null);
        // Accommodation acc = accommodationRepository.findById(request.getAccommodationId()).orElse(null);
        // List<User> guests = ...
        // return reservationService.createReservation(user, acc, request.getStartDate(), request.getEndDate(), guests);
        return null; // Implementación pendiente
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> getReservationsByUser(@PathVariable Long userId) {
        return reservationService.getReservationsByUser(userId);
    }

    @PutMapping("/{id}/cancel")
    public boolean cancelReservation(@PathVariable Long id, @RequestBody CancelRequest request) {
        // User user = userRepository.findById(request.getUserId()).orElse(null);
        // return reservationService.cancelReservation(id, user);
        return false; // Implementación pendiente
    }

    // DTO para la petición de reserva
    public static class ReservationRequest {
        private Long userId;
        private Long accommodationId;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<Long> guestIds;
        // Getters y setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getAccommodationId() { return accommodationId; }
        public void setAccommodationId(Long accommodationId) { this.accommodationId = accommodationId; }
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public List<Long> getGuestIds() { return guestIds; }
        public void setGuestIds(List<Long> guestIds) { this.guestIds = guestIds; }
    }
    public static class CancelRequest {
        private Long userId;
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }
}
