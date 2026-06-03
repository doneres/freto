package com.freto.usuarioService.model;

import java.util.UUID;

import com.freto.usuarioService.model.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Users")
@Table(name = "tb_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}
