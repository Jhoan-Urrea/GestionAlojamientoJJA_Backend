package com.uniquindio.alojamientosAPI.domain.mapper;

import com.uniquindio.alojamientosAPI.domain.dto.ReservationDTO;
import com.uniquindio.alojamientosAPI.persistence.entity.Reservation;

public class ReservationMapper {
    public static ReservationDTO toDTO(Reservation reserva) {
        if (reserva == null) return null;
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reserva.getId());
        dto.setUserId(reserva.getUser() != null ? reserva.getUser().getId() : null);
        dto.setAccommodationId(reserva.getAccommodation() != null ? reserva.getAccommodation().getId() : null);
        dto.setState(reserva.getState() != null ? reserva.getState().getName() : null);
        dto.setStartDate(reserva.getStartDate());
        dto.setEndDate(reserva.getEndDate());
        if (reserva.getGuests() != null) {
            dto.setGuestIds(reserva.getGuests().stream()
                .map(g -> g.getGuest() != null ? g.getGuest().getId() : null)
                .toList());
        }
        return dto;
    }
}

