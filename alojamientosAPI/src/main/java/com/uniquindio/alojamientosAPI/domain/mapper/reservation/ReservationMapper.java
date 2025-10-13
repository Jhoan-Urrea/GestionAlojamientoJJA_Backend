package com.uniquindio.alojamientosAPI.domain.mapper.reservation;

import com.uniquindio.alojamientosAPI.domain.dto.reservation.ReservationResponse;
import com.uniquindio.alojamientosAPI.persistence.entity.reservation.ReservationEntity;

public final class ReservationMapper {

    private ReservationMapper() {}

    public static ReservationResponse toResponse(ReservationEntity e) {
        if (e == null) return null;
        ReservationResponse r = new ReservationResponse();
        r.setId(e.getId());
        r.setUserId(e.getUser() != null ? e.getUser().getId() : null);
        r.setAccommodationId(e.getAccommodation() != null ? e.getAccommodation().getId() : null);
        r.setCheckIn(e.getCheckIn());
        r.setCheckOut(e.getCheckOut());
        r.setCountRoommates(e.getCountRoommates());
        r.setStateName(e.getState() != null ? e.getState().getName() : null);
        return r;
    }
}

