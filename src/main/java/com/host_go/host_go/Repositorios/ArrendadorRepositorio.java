package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Cuenta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrendadorRepositorio extends JpaRepository<Arrendador, Integer> {
    boolean existsByCorreo(String correo); 
    Optional<Arrendador> findByArrendadorId(long arrendador_id);
    Optional<Arrendador> findByCorreo(String correo);
    Optional<Arrendador> findByCuenta(Cuenta cuenta);
}