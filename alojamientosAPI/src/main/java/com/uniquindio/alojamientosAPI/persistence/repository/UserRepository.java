package com.uniquindio.alojamientosAPI.persistence.repository;

import com.uniquindio.alojamientosAPI.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}