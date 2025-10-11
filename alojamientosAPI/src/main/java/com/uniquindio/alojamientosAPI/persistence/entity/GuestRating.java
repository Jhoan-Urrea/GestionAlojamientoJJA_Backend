package com.uniquindio.alojamientosAPI.persistence.entity;

import jakarta.persistence.*;

@Entity
public class GuestRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Reservation reservation;

    @ManyToOne
    private com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity guest;

    private int rating; // Puntuación

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
