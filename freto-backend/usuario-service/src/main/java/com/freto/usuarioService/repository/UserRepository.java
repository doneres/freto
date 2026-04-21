package com.freto.usuarioService.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freto.usuarioService.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);
}
