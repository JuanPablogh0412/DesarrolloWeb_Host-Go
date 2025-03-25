package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Propiedad;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PropiedadRepositorio extends JpaRepository<Propiedad,Long>{
    Optional<Propiedad> findByNombreYUbicacion(String nombre, String ubicacion);
}
