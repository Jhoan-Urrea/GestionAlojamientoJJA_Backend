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

            // ✅ Crear roles si no existen
            RoleEntity roleCliente = roleRepository.findByName(RoleEnum.CLIENTE)
                    .orElseGet(() -> {
                        RoleEntity newRole = new RoleEntity(RoleEnum.CLIENTE, "Cliente del sistema");
                        roleRepository.save(newRole);
                        System.out.println("✅ Rol CLIENTE creado");
                        return newRole;
                    });

            RoleEntity roleAnfitrion = roleRepository.findByName(RoleEnum.ANFITRION)
                    .orElseGet(() -> {
                        RoleEntity newRole = new RoleEntity(RoleEnum.ANFITRION, "Anfitrión de alojamientos");
                        roleRepository.save(newRole);
                        System.out.println("✅ Rol ANFITRION creado");
                        return newRole;
                    });

            RoleEntity roleAdmin = roleRepository.findByName(RoleEnum.ADMINISTRADOR)
                    .orElseGet(() -> {
                        RoleEntity newRole = new RoleEntity(RoleEnum.ADMINISTRADOR, "Administrador del sistema");
                        roleRepository.save(newRole);
                        System.out.println("✅ Rol ADMINISTRADOR creado");
                        return newRole;
                    });

            // ✅ Actualizar contraseñas cifradas de usuarios existentes
            userRepository.findAll().forEach(user -> {
                String currentPassword = user.getPassword();

                // Si la contraseña parece no estar cifrada (no empieza con "$2a$" o "$2b$")
                if (!currentPassword.startsWith("$2a$") && !currentPassword.startsWith("$2b$")) {
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
                rolesAdmin.add(roleAdmin);
                rolesAdmin.add(roleCliente);
                admin.setRoles(rolesAdmin);

                userRepository.save(admin);
                System.out.println("✅ Usuario admin creado correctamente.");
            } else {
                System.out.println("ℹ️ El usuario admin ya existe. No se creará uno nuevo.");
            }

            System.out.println("🚀 Inicialización completada y contraseñas actualizadas correctamente.");
        };
    }
}
