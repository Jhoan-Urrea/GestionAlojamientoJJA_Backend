package com.uniquindio.alojamientosAPI.domain.services.impl;

import com.uniquindio.alojamientosAPI.domain.services.ReviewService;
import com.uniquindio.alojamientosAPI.persistence.entity.GuestRating;
import com.uniquindio.alojamientosAPI.persistence.entity.ReviewComment;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import com.uniquindio.alojamientosAPI.persistence.repository.GuestRatingRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.ReviewCommentRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.ReservationRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.UserRepository;
import com.uniquindio.alojamientosAPI.domain.exception.EntidadNoEncontradaException;
import com.uniquindio.alojamientosAPI.domain.exception.UsuarioNoAutorizadoException;
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
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Reserva no encontrada"));
        var guest = userRepository.findById(guestId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario (huésped) no encontrado"));
        // Validar que el huésped sea el propietario de la reserva
        if (reservation.getUser() == null || reservation.getUser().getId() == null || !reservation.getUser().getId().equals(guestId)) {
            throw new UsuarioNoAutorizadoException("Solo el propietario de la reserva puede calificarla");
        }
        // Validar que la reserva esté finalizada
        if (reservation.getState() == null || reservation.getState().getName() == null ||
                !"FINALIZADA".equalsIgnoreCase(reservation.getState().getName())) {
            throw new IllegalArgumentException("Solo se puede calificar una reserva finalizada");
        }
        // Evitar duplicidad
        if (reservation.getGuestRating() != null) {
            throw new IllegalStateException("La reserva ya tiene una calificación registrada");
        }
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
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Reserva no encontrada"));
        var guest = userRepository.findById(guestId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario (huésped) no encontrado"));
        // Validar que el huésped sea el propietario de la reserva
        if (reservation.getUser() == null || reservation.getUser().getId() == null || !reservation.getUser().getId().equals(guestId)) {
            throw new UsuarioNoAutorizadoException("Solo el propietario de la reserva puede comentarla");
        }
        // Validar que la reserva esté finalizada
        if (reservation.getState() == null || reservation.getState().getName() == null ||
                !"FINALIZADA".equalsIgnoreCase(reservation.getState().getName())) {
            throw new IllegalArgumentException("Solo se puede comentar una reserva finalizada");
        }
        // Evitar duplicidad
        if (reservation.getReviewComment() != null) {
            throw new IllegalStateException("La reserva ya tiene un comentario registrado");
        }
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
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Reserva no encontrada"));
        if (guest == null || guest.getId() == null) {
            throw new EntidadNoEncontradaException("Usuario (huésped) no encontrado");
        }
        if (reservation.getUser() == null || reservation.getUser().getId() == null || !reservation.getUser().getId().equals(guest.getId())) {
            throw new UsuarioNoAutorizadoException("Solo el propietario de la reserva puede calificarla");
        }
        if (reservation.getState() == null || reservation.getState().getName() == null ||
                !"FINALIZADA".equalsIgnoreCase(reservation.getState().getName())) {
            throw new IllegalArgumentException("Solo se puede calificar una reserva finalizada");
        }
        if (reservation.getGuestRating() != null) {
            throw new IllegalStateException("La reserva ya tiene una calificación registrada");
        }
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
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Reserva no encontrada"));
        if (guest == null || guest.getId() == null) {
            throw new EntidadNoEncontradaException("Usuario (huésped) no encontrado");
        }
        if (reservation.getUser() == null || reservation.getUser().getId() == null || !reservation.getUser().getId().equals(guest.getId())) {
            throw new UsuarioNoAutorizadoException("Solo el propietario de la reserva puede comentarla");
        }
        if (reservation.getState() == null || reservation.getState().getName() == null ||
                !"FINALIZADA".equalsIgnoreCase(reservation.getState().getName())) {
            throw new IllegalArgumentException("Solo se puede comentar una reserva finalizada");
        }
        if (reservation.getReviewComment() != null) {
            throw new IllegalStateException("La reserva ya tiene un comentario registrado");
        }
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
