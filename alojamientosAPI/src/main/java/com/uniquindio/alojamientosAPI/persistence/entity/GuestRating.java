package com.uniquindio.alojamientosAPI.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class GuestRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = false)
    private com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity guest;

    @Min(1)
    @Max(5)
    private int rating; // Puntuación

}
