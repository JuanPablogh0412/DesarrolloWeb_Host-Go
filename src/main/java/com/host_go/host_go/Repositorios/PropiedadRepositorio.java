package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Propiedad;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PropiedadRepositorio extends JpaRepository<Propiedad,Long>{
    List<Propiedad> findByNombreContainingIgnoreCaseAndUbicacionContainingIgnoreCaseAndCapacidadGreaterThanEqual(
        String nombre, 
        String ubicacion, 
        int capacidad
    );
    
    @Query("SELECT p FROM Propiedad p WHERE p.propiedad_id = :id")
    Optional<Propiedad> findByIdIncludingDeleted(@Param("id") Long id);
}

