package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.CaliPropiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.host_go.host_go.modelos.Propiedad;

public interface CaliPropiedadRepositorio extends JpaRepository<CaliPropiedad, Long>{

    long count();

    List<CaliPropiedad> findByPropiedad(Propiedad propiedad);

}
