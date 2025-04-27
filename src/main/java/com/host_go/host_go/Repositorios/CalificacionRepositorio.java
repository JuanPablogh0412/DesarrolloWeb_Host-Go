package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.host_go.host_go.modelos.Cuenta;


public interface CalificacionRepositorio extends JpaRepository<Calificacion, Long>{

    long count();

    List<Calificacion> findByCuenta(Cuenta cuenta);

}
