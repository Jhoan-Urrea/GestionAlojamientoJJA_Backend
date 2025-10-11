package com.uniquindio.alojamientosAPI.persistence.entity.accommodation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accommodations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_int")
    private Long id;

    // En esta etapa solo usamos identificadores simples (sin dependencias externas)
    @Column(name = "host_user_id", nullable = false)
    private Long hostUserId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String address;

    @Column(name = "dayprice")
    private Double dayPrice;

    private Integer capacity;

    @Column(name = "state_accommodation_id", nullable = false)
    private Long stateAccommodationId;

    @Column(name = "city_id", nullable = false)
    private Long cityId;
}
