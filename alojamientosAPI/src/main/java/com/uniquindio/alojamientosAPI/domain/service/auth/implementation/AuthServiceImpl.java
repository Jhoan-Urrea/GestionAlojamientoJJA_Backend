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


    /**
     * Servicio encargado de registro del usuario.
     */
    @Override
    @Transactional
    public AuthResponseDTO register(RegisterDTO registerDTO) {
        System.out.println("📝 Iniciando registro para email: " + registerDTO.email());
        
        // Verificar si el email ya existe
        if (userRepository.findByEmail(registerDTO.email()).isPresent()) {
            System.err.println("⚠️ Email ya registrado: " + registerDTO.email());
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
        System.out.println("🔍 Buscando rol CLIENTE...");
        RoleEntity clientRole = roleRepository.findByName(RoleEnum.CLIENTE)
                .orElseThrow(() -> {
                    System.err.println("❌ Rol CLIENTE no encontrado en la base de datos");
                    return new RuntimeException("Rol CLIENTE no encontrado en la base de datos");
                });
        
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(clientRole);
        newUser.setRoles(roles);
        System.out.println("✅ Rol CLIENTE asignado");

        // Guardar usuario
        System.out.println("💾 Guardando usuario en BD...");
        UserEntity savedUser = userRepository.save(newUser);
        System.out.println("✅ Usuario guardado con ID: " + savedUser.getId());

        // Generar token JWT
        System.out.println("🔐 Generando token JWT...");
        UserDetails userDetails = createUserDetails(savedUser);
        String token = jwtUtil.generateToken(userDetails, savedUser.getId());
        System.out.println("✅ Token generado exitosamente");

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
        String token = jwtUtil.generateToken(userDetails, user.getId());

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
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roleNames
        );
    }
}