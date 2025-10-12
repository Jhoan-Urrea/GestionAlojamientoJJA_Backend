package com.uniquindio.alojamientosAPI.domain.service.auth.implementation;

import com.uniquindio.alojamientosAPI.domain.dto.Auth.AuthResponseDTO;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.LoginDTO;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.RegisterDTO;
import com.uniquindio.alojamientosAPI.domain.service.auth.interfaces.AuthService;
import com.uniquindio.alojamientosAPI.persistence.entity.user.RoleEntity;
import com.uniquindio.alojamientosAPI.persistence.entity.user.RoleEnum;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import com.uniquindio.alojamientosAPI.persistence.repository.RoleRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.UserRepository;
import com.uniquindio.alojamientosAPI.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterDTO registerDTO) {
        // Verificar si el email ya existe
        if (userRepository.findByEmail(registerDTO.email()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Crear nueva entidad de usuario
        UserEntity newUser = new UserEntity();
        newUser.setFirstName(registerDTO.firstName());
        newUser.setLastName(registerDTO.lastName());
        newUser.setDayOfBirth(registerDTO.dayOfBirth());
        newUser.setPhoneNumber(registerDTO.phoneNumber());
        newUser.setEmail(registerDTO.email());
        newUser.setPassword(passwordEncoder.encode(registerDTO.password()));
        newUser.setHomeAddress(registerDTO.homeAddress());

        // Asignar rol de CLIENTE por defecto
        RoleEntity clientRole = roleRepository.findByName(RoleEnum.CLIENTE)
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado en la base de datos"));
        
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(clientRole);
        newUser.setRoles(roles);

        // Guardar usuario
        UserEntity savedUser = userRepository.save(newUser);

        // Generar token JWT
        UserDetails userDetails = createUserDetails(savedUser);
        String token = jwtUtil.generateToken(userDetails);

        // Construir respuesta
        return buildAuthResponse(token, savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginDTO loginDTO) {
        // Autenticar usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.email(),
                        loginDTO.password()
                )
        );

        // Obtener usuario autenticado
        UserEntity user = userRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar token JWT
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        // Construir respuesta
        return buildAuthResponse(token, user);
    }

    private UserDetails createUserDetails(UserEntity user) {
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                        .collect(Collectors.toList()))
                .build();
    }

    private AuthResponseDTO buildAuthResponse(String token, UserEntity user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return new AuthResponseDTO(
                token,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roleNames
        );
    }
}