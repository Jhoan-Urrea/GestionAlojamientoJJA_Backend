package com.uniquindio.alojamientosAPI.persistence.repository.accommodation;

import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.AccommodationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {

    List<AccommodationEntity> findByCityId(Long cityId);

    List<AccommodationEntity> findByHostUserId(Long hostUserId);

    List<AccommodationEntity> findByStateAccommodationId(Long stateAccommodationId);
}
