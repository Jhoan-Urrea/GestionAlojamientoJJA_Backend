package com.uniquindio.alojamientosAPI.domain.services;

import com.uniquindio.alojamientosAPI.persistence.entity.Reservation;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    Reservation createReservation(Long userId, Long accommodationId, LocalDate startDate, LocalDate endDate, List<Long> guestIds);
    boolean cancelReservation(Long reservationId, Long userId);
    List<Reservation> getReservationsByUser(Long userId);
}
