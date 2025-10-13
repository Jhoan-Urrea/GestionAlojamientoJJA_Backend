package com.uniquindio.alojamientosAPI.persistence.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JdbcTypeCode(SqlTypes.INTEGER)
    @Column(name = "id_int")
    private Long id;

    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @Column(name = "dayOfBirth")
    private LocalDate dayOfBirth;

    @Column(name = "phoneNumber", length = 50)
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "url_accountPhoto", columnDefinition = "TEXT")
    private String urlAccountPhoto;

    @Column(name = "homeAddress", columnDefinition = "TEXT")
    private String homeAddress;

    // Relación con roles
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id_int"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id_int")
    )
    private Set<RoleEntity> roles;
}
