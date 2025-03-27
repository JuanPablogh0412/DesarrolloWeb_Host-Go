package com.host_go.host_go.Repositorios;
import com.host_go.host_go.modelos.Solicitud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SolicitudRepositorio extends JpaRepository<Solicitud, Long>, JpaSpecificationExecutor<Solicitud>{
List<Solicitud> findByPropiedadPropiedadId(Long propiedadId);
List<Solicitud> findByArrendatarioArrendatarioId(Long arrendatarioId);
}
