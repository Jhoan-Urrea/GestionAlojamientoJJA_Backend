package com.uniquindio.alojamientosAPI.web.auth;

import com.uniquindio.alojamientosAPI.security.auth.CustomUserDetails;
import com.uniquindio.alojamientosAPI.security.jwt.JwtService;
import com.uniquindio.alojamientosAPI.web.auth.dto.AuthResponse;
import com.uniquindio.alojamientosAPI.web.auth.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();
            String token = jwtService.generateToken(principal);
            long expiresInMs = jwtService.extractExpiration(token).getTime() - System.currentTimeMillis();
            List<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

            return ResponseEntity.ok(AuthResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .expiresAt(Instant.ofEpochMilli(System.currentTimeMillis() + expiresInMs))
                    .roles(roles)
                    .email(principal.getUsername())
                    .build());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(
                java.util.Map.of(
                        "email", principal.getUsername(),
                        "roles", principal.getAuthorities()
                )
        );
    }
}
