package com.uniquindio.alojamientosAPI.domain.service.reservation;

import com.uniquindio.alojamientosAPI.persistence.entity.reservation.ReservationEntity;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    ReservationEntity createReservation(Long userId,
                                        Long accommodationId,
                                        LocalDate checkIn,
                                        LocalDate checkOut,
                                        Integer countRoommates);

    void cancelReservation(Long reservationId, Long userId);

    List<ReservationEntity> listByUser(Long userId);
}

