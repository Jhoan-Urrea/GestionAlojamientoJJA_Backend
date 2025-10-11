package com.uniquindio.alojamientosAPI.persistence.repository;

import com.uniquindio.alojamientosAPI.persistence.entity.StateReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateReservationRepository extends JpaRepository<StateReservation, Long> {
}

