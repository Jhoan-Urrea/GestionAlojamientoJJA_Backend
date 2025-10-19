package com.uniquindio.alojamientosAPI.config;

import com.uniquindio.alojamientosAPI.persistence.entity.user.RoleEntity;
import com.uniquindio.alojamientosAPI.persistence.entity.user.RoleEnum;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import com.uniquindio.alojamientosAPI.persistence.repository.RoleRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository,
                                       RoleRepository roleRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {

            System.out.println("🔐 Iniciando proceso de verificación y actualización de contraseñas...");

            // Sembrar roles base si faltan (idempotente)
            for (RoleEnum roleEnum : new RoleEnum[]{RoleEnum.ADMINISTRADOR, RoleEnum.CLIENTE, RoleEnum.ANFITRION}) {
                roleRepository.findByName(roleEnum).orElseGet(() -> {
                    RoleEntity r = RoleEntity.builder()
                            .name(roleEnum)
                            .description("Rol base " + roleEnum.name())
                            .build();
                    roleRepository.save(r);
                    System.out.println("✅ Rol creado: " + roleEnum.name());
                    return r;
                });
            }

            Optional<RoleEntity> roleUserOpt = roleRepository.findByName(RoleEnum.CLIENTE);
            Optional<RoleEntity> roleAdminOpt = roleRepository.findByName(RoleEnum.ADMINISTRADOR);
            RoleEntity roleUser = roleUserOpt.orElse(null);
            RoleEntity roleAdmin = roleAdminOpt.orElse(null);

            // ✅ Actualizar contraseñas cifradas de usuarios existentes
            userRepository.findAll().forEach(user -> {
                String currentPassword = user.getPassword();

                // Si la contraseña parece no estar cifrada (no empieza con "$2a$", "$2b$" o "$2y$")
                if (currentPassword != null
                        && !currentPassword.startsWith("$2a$")
                        && !currentPassword.startsWith("$2b$")
                        && !currentPassword.startsWith("$2y$")) {
                    String encodedPassword = passwordEncoder.encode(currentPassword);
                    user.setPassword(encodedPassword);
                    userRepository.save(user);
                    System.out.println("🔄 Contraseña cifrada para usuario: " + user.getEmail());
                }
            });

            // ✅ Crear usuario admin solo si no existe
            Optional<UserEntity> existingAdmin = userRepository.findByEmail("admin@example.com");
            if (existingAdmin.isEmpty()) {
                UserEntity admin = new UserEntity();
                admin.setFirstName("Admin");
                admin.setLastName("Principal");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin1234"));
                admin.setPhoneNumber("3001111111");
                admin.setHomeAddress("Calle 999");

                Set<RoleEntity> rolesAdmin = new HashSet<>();
                if (roleAdmin != null) rolesAdmin.add(roleAdmin);
                if (roleUser != null) rolesAdmin.add(roleUser);
                admin.setRoles(rolesAdmin);

                userRepository.save(admin);
                System.out.println("✅ Usuario admin creado correctamente.");
            } else {
                System.out.println("ℹ️ El usuario admin ya existe. No se creará uno nuevo.");
                // Si existe pero no tiene roles asignados, asignar roles base de forma segura
                UserEntity admin = existingAdmin.get();
                if (admin.getRoles() == null || admin.getRoles().isEmpty()) {
                    Set<RoleEntity> rolesAdmin = new HashSet<>();
                    if (roleAdmin != null) rolesAdmin.add(roleAdmin);
                    if (roleUser != null) rolesAdmin.add(roleUser);
                    admin.setRoles(rolesAdmin);
                    userRepository.save(admin);
                    System.out.println("✅ Roles base asignados al admin existente.");
                }
            }

            System.out.println("🚀 Inicialización completada y contraseñas actualizadas correctamente.");
        };
    }
}
