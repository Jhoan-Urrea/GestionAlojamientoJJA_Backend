package com.uniquindio.alojamientosAPI.security.auth;

import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Implementar según negocio si se requiere
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Implementar según negocio si se requiere
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Implementar según negocio si se requiere
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
