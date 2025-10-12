package com.uniquindio.alojamientosAPI.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ReviewComment {
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

    @NotBlank
    private String comment;

}
