package com.uniquindio.alojamientosAPI.persistence.entity;

import jakarta.persistence.*;

@Entity
public class StateReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // Ejemplo: PENDIENTE, CONFIRMADA, CANCELADA, FINALIZADA

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
