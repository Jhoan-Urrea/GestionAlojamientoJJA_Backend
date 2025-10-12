package com.uniquindio.alojamientosAPI.persistence.repository.reservation;

import com.uniquindio.alojamientosAPI.persistence.entity.reservation.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByUser_Id(Long userId);

    List<ReservationEntity> findByAccommodation_Id(Long accommodationId);

    // Útil para validar superposición (se usará en Commit 3)
    boolean existsByAccommodation_IdAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
            Long accommodationId,
            LocalDate checkOut,
            LocalDate checkIn
    );
}

