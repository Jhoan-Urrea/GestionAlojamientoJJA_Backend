package com.uniquindio.alojamientosAPI.domain.mapper;

import com.uniquindio.alojamientosAPI.domain.dto.ReviewCommentDTO;
import com.uniquindio.alojamientosAPI.persistence.entity.ReviewComment;

public class ReviewCommentMapper {
    public static ReviewCommentDTO toDTO(ReviewComment entity) {
        if (entity == null) return null;
        ReviewCommentDTO dto = new ReviewCommentDTO();
        dto.setId(entity.getId());
        dto.setReservationId(entity.getReservation() != null ? entity.getReservation().getId() : null);
        dto.setGuestId(entity.getGuest() != null ? entity.getGuest().getId() : null);
        dto.setComment(entity.getComment());
        return dto;
    }
}

