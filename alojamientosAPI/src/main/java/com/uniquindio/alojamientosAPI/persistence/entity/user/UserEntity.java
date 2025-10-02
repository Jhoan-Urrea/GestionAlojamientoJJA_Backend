package com.uniquindio.alojamientosAPI.persistence.entity.user;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

    // Getters y setters...

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Set<RoleEntity> getRoles() { return roles; }

    public void setRoles(Set<RoleEntity> roles) { this.roles = roles; }

    // Otros campos y métodos según necesidad
}
