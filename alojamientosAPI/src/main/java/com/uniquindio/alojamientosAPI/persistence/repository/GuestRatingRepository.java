package com.uniquindio.alojamientosAPI.persistence.repository;

import com.uniquindio.alojamientosAPI.persistence.entity.GuestRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRatingRepository extends JpaRepository<GuestRating, Long> {
}

