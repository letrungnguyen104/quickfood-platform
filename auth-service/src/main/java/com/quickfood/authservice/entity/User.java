package com.quickfood.authservice.entity;

import com.quickfood.authservice.enums.AuthProvider;
import com.quickfood.authservice.enums.Role;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = true)
    private String username;

    @Column(nullable = true)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private Role role = Role.ROLE_CUSTOMER;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Builder.Default
    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Builder.Default
    @Column(nullable = false)
    private String status = "ACTIVE";

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL;

    @Column(name = "provider_id")
    private String providerId;
}