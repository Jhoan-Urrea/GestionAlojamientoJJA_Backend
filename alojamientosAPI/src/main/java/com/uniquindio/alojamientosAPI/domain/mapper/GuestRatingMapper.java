package com.uniquindio.alojamientosAPI.domain.mapper;

import com.uniquindio.alojamientosAPI.domain.dto.GuestRatingDTO;
import com.uniquindio.alojamientosAPI.persistence.entity.GuestRating;

public class GuestRatingMapper {
    public static GuestRatingDTO toDTO(GuestRating entity) {
        if (entity == null) return null;
        GuestRatingDTO dto = new GuestRatingDTO();
        dto.setId(entity.getId());
        dto.setReservationId(entity.getReservation() != null ? entity.getReservation().getId() : null);
        dto.setGuestId(entity.getGuest() != null ? entity.getGuest().getId() : null);
        dto.setRating(entity.getRating());
        return dto;
    }
}

