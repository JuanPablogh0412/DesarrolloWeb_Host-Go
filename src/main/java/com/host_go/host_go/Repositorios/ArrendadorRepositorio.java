package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Arrendador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrendadorRepositorio extends JpaRepository<Arrendador, Integer> {
    boolean existsByCorreo(String correo); 
}