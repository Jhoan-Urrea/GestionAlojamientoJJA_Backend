package com.uniquindio.alojamientosAPI.persistence.repository.accommodation;

import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    // Obtener todos los favoritos de un usuario específico
    List<FavoriteEntity> findByUser_Id(Long userId);

    // Verificar si un alojamiento ya está en favoritos del usuario
    boolean existsByUser_IdAndAccommodation_Id(Long userId, Long accommodationId);

    // Eliminar un favorito específico del usuario
    void deleteByUser_IdAndAccommodation_Id(Long userId, Long accommodationId);
}
