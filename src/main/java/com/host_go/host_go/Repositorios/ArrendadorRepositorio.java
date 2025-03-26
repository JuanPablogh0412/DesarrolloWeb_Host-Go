package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Cuenta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ArrendadorRepositorio extends JpaRepository<Arrendador, Long> {
    boolean existsByCorreo(String correo); 
    boolean existsByArrendadorId(long arrendadorId);
    Optional<Arrendador> findByArrendadorId(long arrendadorId);
    Optional<Arrendador> findByCorreo(String correo);
    Optional<Arrendador> findByCuenta(Cuenta cuenta);
}