package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Arrendatario;
import com.host_go.host_go.modelos.Cuenta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrendatarioRepositorio extends JpaRepository<Arrendatario, Integer>{
boolean existsByCorreo(String correo); 
    Optional<Arrendatario> findByCorreo(String correo);
    Optional<Arrendatario> findByCuenta(Cuenta cuenta);
}
