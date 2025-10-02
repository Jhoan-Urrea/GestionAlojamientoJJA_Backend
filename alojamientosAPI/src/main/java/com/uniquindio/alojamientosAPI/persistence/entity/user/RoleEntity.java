package com.uniquindio.alojamientosAPI.persistence.entity.user;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleEnum name;

    public RoleEntity() {}
    public RoleEntity(RoleEnum name) { this.name = name; }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RoleEnum getName() { return name; }
    public void setName(RoleEnum name) { this.name = name; }
}