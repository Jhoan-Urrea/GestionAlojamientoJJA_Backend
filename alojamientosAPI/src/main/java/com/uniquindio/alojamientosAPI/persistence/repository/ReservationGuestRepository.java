package com.uniquindio.alojamientosAPI.persistence.repository;

import com.uniquindio.alojamientosAPI.persistence.entity.ReservationGuest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationGuestRepository extends JpaRepository<ReservationGuest, Long> {
}

