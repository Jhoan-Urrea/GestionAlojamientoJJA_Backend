package com.uniquindio.alojamientosAPI.persistence.entity.reservation;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "guest_ratings")
public class GuestRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_int")
    private Long id;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    // Usuario que realiza la calificación (p.ej., anfitrión)
    @Column(name = "rater_user_id", nullable = false)
    private Long raterUserId;

    // Usuario calificado (p.ej., huésped de la reserva)
    @Column(name = "rated_user_id", nullable = false)
    private Long ratedUserId;

    @Column(name = "score", nullable = false)
    private Integer score; // 1-5

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

