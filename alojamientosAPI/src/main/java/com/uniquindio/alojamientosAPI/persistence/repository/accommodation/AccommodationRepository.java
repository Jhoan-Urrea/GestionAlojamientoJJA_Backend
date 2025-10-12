package com.uniquindio.alojamientosAPI.persistence.repository.accommodation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.AccommodationEntity;

import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {

    // Buscar alojamientos por ciudad
    List<AccommodationEntity> findByCityId(Long cityId);

    // Buscar alojamientos por usuario anfitrión
    List<AccommodationEntity> findByHostUserId(Long hostUserId);

    // Buscar alojamientos por estado
    List<AccommodationEntity> findByStateAccommodationId(Long stateAccommodationId);
}
