package com.uniquindio.alojamientosAPI.persistence.entity.accommodation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pictures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PictureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_int")
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "ismain")
    @Builder.Default
    private Boolean isMain = false;

    @Column(columnDefinition = "TEXT")
    private String url;

    // Referencia por ID simple para mantener independencia del módulo
    @Column(name = "accommodation_id", nullable = false)
    private Long accommodationId;
}
