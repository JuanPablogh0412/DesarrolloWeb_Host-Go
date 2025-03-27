package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Cuenta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepositorio extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByUsuario(String usuario);
}

