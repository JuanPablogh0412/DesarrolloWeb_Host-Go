package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Arrendatario;
import com.host_go.host_go.modelos.Cuenta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrendatarioRepositorio extends JpaRepository<Arrendatario, Long>{
boolean existsByCorreo(String correo); 
    Optional<Arrendatario> findByArrendatarioId(long arrendatarioId);
    Optional<Arrendatario> findByCorreo(String correo);
    Optional<Arrendatario> findByCuenta(Cuenta cuenta);
}
