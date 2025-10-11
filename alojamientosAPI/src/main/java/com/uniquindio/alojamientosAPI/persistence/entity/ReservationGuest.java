package com.uniquindio.alojamientosAPI.persistence.entity;

import jakarta.persistence.*;

@Entity
public class ReservationGuest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Reservation reservation;

    @ManyToOne
    private com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity guest; // Usuario invitado

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity getGuest() {
        return guest;
    }

    public void setGuest(com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity guest) {
        this.guest = guest;
    }
}
