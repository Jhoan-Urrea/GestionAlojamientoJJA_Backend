package com.uniquindio.alojamientosAPI.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import java.util.List;
import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@Entity
public class Reservation {
    // Getters y setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private UserEntity user; // Cliente que realiza la reserva

    @NotNull
    @ManyToOne
    private Accommodation accommodation;

    @NotNull
    @ManyToOne
    private StateReservation state;

    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    @NotNull
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private java.util.List<com.uniquindio.alojamientosAPI.persistence.entity.ReservationGuest> guests;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private GuestRating guestRating;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private ReviewComment reviewComment;

}
