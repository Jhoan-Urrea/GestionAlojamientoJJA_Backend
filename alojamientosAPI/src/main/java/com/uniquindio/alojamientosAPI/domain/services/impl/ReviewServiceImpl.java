package com.uniquindio.alojamientosAPI.domain.services.impl;

import com.uniquindio.alojamientosAPI.domain.services.ReviewService;
import com.uniquindio.alojamientosAPI.persistence.entity.GuestRating;
import com.uniquindio.alojamientosAPI.persistence.entity.ReviewComment;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import com.uniquindio.alojamientosAPI.persistence.repository.GuestRatingRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.ReviewCommentRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.ReservationRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private GuestRatingRepository guestRatingRepository;
    @Autowired
    private ReviewCommentRepository reviewCommentRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public GuestRating rateReservation(Long reservationId, Long guestId, int rating) {
        var reservation = reservationRepository.findById(reservationId).orElse(null);
        var guest = userRepository.findById(guestId).orElse(null);
        if (reservation == null || guest == null) return null;
        // Validar que la reserva esté finalizada
        if (!"FINALIZADA".equalsIgnoreCase(reservation.getState().getName())) return null;
        // Validar que no exista calificación previa
        if (reservation.getGuestRating() != null) return null;
        // Crear y guardar la calificación
        GuestRating guestRating = new GuestRating();
        guestRating.setReservation(reservation);
        guestRating.setGuest(guest);
        guestRating.setRating(rating);
        guestRatingRepository.save(guestRating);
        reservation.setGuestRating(guestRating);
        reservationRepository.save(reservation);
        return guestRating;
    }

    @Override
    public ReviewComment commentReservation(Long reservationId, Long guestId, String comment) {
        var reservation = reservationRepository.findById(reservationId).orElse(null);
        var guest = userRepository.findById(guestId).orElse(null);
        if (reservation == null || guest == null) return null;
        // Validar que la reserva esté finalizada
        if (!"FINALIZADA".equalsIgnoreCase(reservation.getState().getName())) return null;
        // Validar que no exista comentario previo
        if (reservation.getReviewComment() != null) return null;
        // Crear y guardar el comentario
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setReservation(reservation);
        reviewComment.setGuest(guest);
        reviewComment.setComment(comment);
        reviewCommentRepository.save(reviewComment);
        reservation.setReviewComment(reviewComment);
        reservationRepository.save(reservation);
        return reviewComment;
    }

    @Override
    public GuestRating rateReservation(Long reservationId, UserEntity guest, int rating) {
        var reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation == null || guest == null) return null;
        if (!"FINALIZADA".equalsIgnoreCase(reservation.getState().getName())) return null;
        if (reservation.getGuestRating() != null) return null;
        GuestRating guestRating = new GuestRating();
        guestRating.setReservation(reservation);
        guestRating.setGuest(guest);
        guestRating.setRating(rating);
        guestRatingRepository.save(guestRating);
        reservation.setGuestRating(guestRating);
        reservationRepository.save(reservation);
        return guestRating;
    }

    @Override
    public ReviewComment commentReservation(Long reservationId, UserEntity guest, String comment) {
        var reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation == null || guest == null) return null;
        if (!"FINALIZADA".equalsIgnoreCase(reservation.getState().getName())) return null;
        if (reservation.getReviewComment() != null) return null;
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setReservation(reservation);
        reviewComment.setGuest(guest);
        reviewComment.setComment(comment);
        reviewCommentRepository.save(reviewComment);
        reservation.setReviewComment(reviewComment);
        reservationRepository.save(reservation);
        return reviewComment;
    }
}
