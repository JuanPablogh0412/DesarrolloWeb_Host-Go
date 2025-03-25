package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalificacionRepositorio extends JpaRepository<Calificacion, Long>{

    long count();

}
