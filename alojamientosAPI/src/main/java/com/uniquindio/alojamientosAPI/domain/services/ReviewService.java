package com.uniquindio.alojamientosAPI.domain.services;

import com.uniquindio.alojamientosAPI.persistence.entity.GuestRating;
import com.uniquindio.alojamientosAPI.persistence.entity.ReviewComment;
import com.uniquindio.alojamientosAPI.persistence.entity.User;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;

import java.lang.Long;

public interface ReviewService {
    GuestRating rateReservation(Long reservationId, Long guestId, int rating);
    ReviewComment commentReservation(Long reservationId, Long guestId, String comment);

    GuestRating rateReservation(Long reservationId, UserEntity guest, int rating);

    ReviewComment commentReservation(Long reservationId, UserEntity guest, String comment);
}
