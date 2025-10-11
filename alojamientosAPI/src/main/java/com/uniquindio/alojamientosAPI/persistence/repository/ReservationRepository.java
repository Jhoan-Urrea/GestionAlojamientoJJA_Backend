package com.uniquindio.alojamientosAPI.persistence.repository;

import com.uniquindio.alojamientosAPI.persistence.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Buscar reservas por usuario
    List<Reservation> findByUser_Id(Long userId);
    // Buscar reservas por alojamiento
    List<Reservation> findByAccommodation_Id(Long accommodationId);
    // Buscar reservas por estado
    List<Reservation> findByState_Name(String stateName);
}
