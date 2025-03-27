package com.host_go.host_go.Repositorios;

import com.host_go.host_go.modelos.Propiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PropiedadRepositorio extends JpaRepository<Propiedad, Long> {
    Optional<Propiedad> findByNombreAndUbicacion(String Nombre, String Ubicacion);
}
