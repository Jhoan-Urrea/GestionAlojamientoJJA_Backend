package com.uniquindio.alojamientosAPI.persistence.repository;

import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    // Puedes agregar métodos personalizados si lo necesitas
}
