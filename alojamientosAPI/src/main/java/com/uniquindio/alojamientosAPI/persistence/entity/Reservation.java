package com.uniquindio.alojamientosAPI.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Reservation {
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

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity getUser() { return user; }
    public void setUser(com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity user) { this.user = user; }
    public Accommodation getAccommodation() { return accommodation; }
    public void setAccommodation(Accommodation accommodation) { this.accommodation = accommodation; }
    public StateReservation getState() { return state; }
    public void setState(StateReservation state) { this.state = state; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public java.util.List<com.uniquindio.alojamientosAPI.persistence.entity.ReservationGuest> getGuests() { return guests; }
    public void setGuests(java.util.List<com.uniquindio.alojamientosAPI.persistence.entity.ReservationGuest> guests) { this.guests = guests; }
    public GuestRating getGuestRating() { return guestRating; }
    public void setGuestRating(GuestRating guestRating) { this.guestRating = guestRating; }
    public ReviewComment getReviewComment() { return reviewComment; }
    public void setReviewComment(ReviewComment reviewComment) { this.reviewComment = reviewComment; }
}
