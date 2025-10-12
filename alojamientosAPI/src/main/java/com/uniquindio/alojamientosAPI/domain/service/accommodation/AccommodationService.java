package com.uniquindio.alojamientosAPI.domain.service.accommodation;

import java.util.List;
import java.util.Optional;

import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.AccommodationEntity;

public interface AccommodationService {

    AccommodationEntity create(AccommodationEntity accommodation);

    AccommodationEntity update(Long id, AccommodationEntity accommodation);

    void delete(Long id);

    List<AccommodationEntity> listAll();

    Optional<AccommodationEntity> findById(Long id);

    List<AccommodationEntity> findByCity(Long cityId);

    List<AccommodationEntity> findByHost(Long hostUserId);
}
