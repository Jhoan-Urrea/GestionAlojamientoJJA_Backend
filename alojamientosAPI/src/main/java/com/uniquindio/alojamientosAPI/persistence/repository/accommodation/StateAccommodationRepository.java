package com.uniquindio.alojamientosAPI.persistence.repository.accommodation;

import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.StateAccommodationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateAccommodationRepository extends JpaRepository<StateAccommodationEntity, Long> {
}
