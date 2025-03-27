package com.host_go.host_go.Repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.host_go.host_go.modelos.ActivationToken;

public interface ActivationTokenRepositorio extends JpaRepository<ActivationToken, Long> {
    Optional<ActivationToken> findByToken(String token);
}