package com.uniquindio.alojamientosAPI.persistence.repository;

import com.uniquindio.alojamientosAPI.persistence.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
}

