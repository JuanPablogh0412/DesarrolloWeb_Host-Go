package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Propiedad;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PropiedadRepositorio extends JpaRepository<Propiedad,Long>{
    List<Propiedad> findByNombreContainingIgnoreCaseAndMunicipioContainingIgnoreCaseAndCapacidadGreaterThanEqual(
        String nombre, 
        String municipio, 
        int capacidad
    );
    List<Propiedad> findByArrendadorArrendadorId(Long arrendadorId);
}

