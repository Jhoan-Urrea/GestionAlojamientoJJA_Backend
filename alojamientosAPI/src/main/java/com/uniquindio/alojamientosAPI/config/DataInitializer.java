package com.uniquindio.alojamientosAPI.config;

import com.uniquindio.alojamientosAPI.domain.user.RoleEntity;
import com.uniquindio.alojamientosAPI.domain.user.RoleEnum;
import com.uniquindio.alojamientosAPI.domain.user.UserEntity;
import com.uniquindio.alojamientosAPI.persistence.repository.RoleRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Crear roles si no existen
            RoleEntity roleUser = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new RoleEntity(RoleEnum.ROLE_USER)));

            RoleEntity roleAdmin = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new RoleEntity(RoleEnum.ROLE_ADMIN)));

            // Crear usuario user1 con rol USER
            if (userRepository.findByUsername("user1").isEmpty()) {
                UserEntity user1 = new UserEntity();
                user1.setUsername("user1");
                user1.setPassword(passwordEncoder.encode("password1"));
                user1.setEnabled(true);
                Set<RoleEntity> rolesUser1 = new HashSet<>();
                rolesUser1.add(roleUser);
                user1.setRoles(rolesUser1);
                userRepository.save(user1);
            }

            // Crear usuario admin con roles ADMIN y USER
            if (userRepository.findByUsername("admin").isEmpty()) {
                UserEntity admin = new UserEntity();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin1234"));
                admin.setEnabled(true);
                Set<RoleEntity> rolesAdmin = new HashSet<>();
                rolesAdmin.add(roleAdmin);
                rolesAdmin.add(roleUser);
                admin.setRoles(rolesAdmin);
                userRepository.save(admin);
            }
        };
    }
}
