package com.uniquindio.alojamientosAPI.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
public class Reservation {
    // Getters y setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity user; // Cliente que realiza la reserva

    @ManyToOne
    private Accommodation accommodation;

    @ManyToOne
    private StateReservation state;

    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private java.util.List<com.uniquindio.alojamientosAPI.persistence.entity.ReservationGuest> guests;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private GuestRating guestRating;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private ReviewComment reviewComment;

}
