package com.host_go.host_go.Servicios;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.SolicitudDto;
import com.host_go.host_go.Repositorios.ArrendatarioRepositorio;
import com.host_go.host_go.Repositorios.PropiedadRepositorio;
import com.host_go.host_go.Repositorios.SolicitudRepositorio;
import com.host_go.host_go.modelos.Arrendatario;
import com.host_go.host_go.modelos.Propiedad;
import com.host_go.host_go.modelos.Solicitud;
import com.host_go.host_go.modelos.Status;

@Service
public class SolicitudServicio {

    @Autowired
    SolicitudRepositorio SolicitudRepositorio;
    @Autowired
    private PropiedadRepositorio propiedadRepositorio;
    @Autowired
    private ArrendatarioRepositorio arrendatarioRepositorio;
    @Autowired
    ModelMapper modelMapper;

    public SolicitudDto get(Long id) {
        Optional<Solicitud> SolicitudOptional = SolicitudRepositorio.findById(id);
        SolicitudDto SolicitudDto = null;
        if (SolicitudOptional != null) {
            SolicitudDto = modelMapper.map(SolicitudOptional.get(), SolicitudDto.class);
        }
        return SolicitudDto;
    }

    public List<SolicitudDto> get() {
        List<Solicitud> Solicituds = (List<Solicitud>) SolicitudRepositorio.findAll();
        List<SolicitudDto> SolicitudDtos = Solicituds.stream()
                .map(Solicitud -> modelMapper.map(Solicitud, SolicitudDto.class)).collect(Collectors.toList());
        return SolicitudDtos;
    }

    public SolicitudDto save(SolicitudDto SolicitudDto) {
        validarFechas(SolicitudDto.getFechaInicio(), SolicitudDto.getFechaFin());
        Solicitud solicitud = modelMapper.map(SolicitudDto, Solicitud.class);
        Propiedad propiedad = propiedadRepositorio.findByPropiedadId(SolicitudDto.getPropiedad().getPropiedadId())
                .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrado"));
        solicitud.setPropiedad(propiedad);
    
        Arrendatario arrendatario = arrendatarioRepositorio
                .findByArrendatarioId(SolicitudDto.getArrendatario().getArrendatarioId())
                .orElseThrow(() -> new IllegalArgumentException("Arrendador no encontrado"));
        solicitud.setArrendatario(arrendatario);
    
        validarCapacidad(propiedad, SolicitudDto.getCantidadPer());
        int costo = calcularCostoTotal(propiedad, SolicitudDto.getFechaInicio(), SolicitudDto.getFechaFin());
        solicitud.setCostoTotal(costo);
        solicitud.setStatus(Status.INACTIVE);
        solicitud = SolicitudRepositorio.save(solicitud);
        return modelMapper.map(solicitud, SolicitudDto.class);
    }
    

    public SolicitudDto update(SolicitudDto SolicitudDto) throws ValidationException {
        SolicitudDto = get(SolicitudDto.getSolicitudId());
        if (SolicitudDto == null) {
            throw new ValidationException(null);
        }
        Solicitud Solicitud = modelMapper.map(SolicitudDto, Solicitud.class);
        Solicitud.setStatus(Status.INACTIVE);
        Solicitud = SolicitudRepositorio.save(Solicitud);
        SolicitudDto = modelMapper.map(Solicitud, SolicitudDto.class);
        return SolicitudDto;
    }

    public void delete(Long id) {
        SolicitudRepositorio.deleteById(id);
    }

    private void validarFechas(String fechaInicio, String fechaFin) {
        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);

        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        }
    }

    private int calcularCostoTotal(Propiedad propiedad, String fechaInicio, String fechaFin) {
        long dias = ChronoUnit.DAYS.between(
                LocalDate.parse(fechaInicio),
                LocalDate.parse(fechaFin));
        return propiedad.getValorNoche() * (int) dias;
    }

    private void validarCapacidad(Propiedad propiedad, int cantidadPersonas) {
        if (cantidadPersonas > propiedad.getCapacidad()) {
            throw new IllegalArgumentException("La cantidad de personas excede la capacidad de la propiedad");
        }
    }

    public List<SolicitudDto> obtenerSolicitudesPorArrendatario(Long arrendatarioId) {
        // Se consulta en el repositorio todas las solicitudes realizadas por el arrendatario
        List<Solicitud> solicitudes = SolicitudRepositorio.findByArrendatarioArrendatarioId(arrendatarioId);
        return solicitudes.stream()
                .map(s -> modelMapper.map(s, SolicitudDto.class))
                .collect(Collectors.toList());
    }

    public List<SolicitudDto> obtenerSolicitudesPorPropiedadYArrendador(Long propiedadId, Long arrendadorId) {
        Propiedad propiedad = propiedadRepositorio.findById(propiedadId)
            .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));
    
        if (!(propiedad.getArrendador().getArrendadorId() ==arrendadorId)) {
            throw new SecurityException("No tienes permisos para ver estas solicitudes");
        }
    
        List<Solicitud> solicitudes = SolicitudRepositorio.findByPropiedadPropiedadId(propiedadId);
    
        return solicitudes.stream()
            .map(s -> modelMapper.map(s, SolicitudDto.class))
            .collect(Collectors.toList());
    }

    public SolicitudDto aceptarSolicitud(Long solicitudId) {
        Solicitud solicitud = SolicitudRepositorio.findById(solicitudId)
            .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        
        if (!solicitud.getStatus().equals(Status.INACTIVE)) {
            throw new IllegalStateException("La solicitud no se encuentra en estado INACTIVE y no se puede aceptar");
        }
        
        solicitud.setStatus(Status.ACTIVE);
        solicitud = SolicitudRepositorio.save(solicitud);
        return modelMapper.map(solicitud, SolicitudDto.class);
    }
    
    public SolicitudDto cancelarSolicitud(Long solicitudId) {
        Solicitud solicitud = SolicitudRepositorio.findById(solicitudId)
            .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        
        if (!solicitud.getStatus().equals(Status.INACTIVE)) {
            throw new IllegalStateException("La solicitud no se encuentra en estado INACTIVE y no se puede cancelar");
        }
        
        solicitud.setStatus(Status.DELETED);
        solicitud = SolicitudRepositorio.save(solicitud);
        return modelMapper.map(solicitud, SolicitudDto.class);
    }
    
    
    
}
