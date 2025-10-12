package com.uniquindio.alojamientosAPI.persistence.repository.reservation;

import com.uniquindio.alojamientosAPI.persistence.entity.reservation.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByUser_Id(Long userId);

    List<ReservationEntity> findByAccommodation_Id(Long accommodationId);

    // Útil para validar superposición (estricta): permite back-to-back
    boolean existsByAccommodation_IdAndCheckInLessThanAndCheckOutGreaterThan(
            Long accommodationId,
            LocalDate checkOut,
            LocalDate checkIn
    );

    // Opción inclusiva (bloquea back-to-back): se deja por si se necesita
    boolean existsByAccommodation_IdAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
            Long accommodationId,
            LocalDate checkOut,
            LocalDate checkIn
    );
}
