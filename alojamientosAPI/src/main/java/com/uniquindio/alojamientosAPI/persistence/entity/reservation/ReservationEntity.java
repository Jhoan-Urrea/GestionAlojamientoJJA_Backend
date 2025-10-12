package com.uniquindio.alojamientosAPI.persistence.entity.reservation;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_int")
    private Long id;

    // Referencias por ID para evitar acoplamiento con otros módulos
    @Column(name = "accommodation_id", nullable = false)
    private Long accommodationId;

    @Column(name = "customer_user_id", nullable = false)
    private Long customerUserId;

    @Column(name = "state_reservation_id", nullable = false)
    private Long stateReservationId;

    @Column(name = "checkin_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "checkout_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "guests_count", nullable = false)
    private Integer guestsCount;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
