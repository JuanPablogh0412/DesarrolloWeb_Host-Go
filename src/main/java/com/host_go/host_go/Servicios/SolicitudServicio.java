package com.host_go.host_go.Servicios;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.SolicitudDto;
import com.host_go.host_go.Repositorios.ArrendatarioRepositorio;
import com.host_go.host_go.Repositorios.PropiedadRepositorio;
import com.host_go.host_go.Repositorios.SolicitudRepositorio;
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

    public SolicitudDto get(Long id){
        Optional<Solicitud> SolicitudOptional = SolicitudRepositorio.findById(id);
        SolicitudDto SolicitudDto = null;
        if( SolicitudOptional != null){
            SolicitudDto = modelMapper.map(SolicitudOptional.get(), SolicitudDto.class);
        }
        return SolicitudDto;
    }

    public List<SolicitudDto> get( ){
        List<Solicitud>Solicituds = (List<Solicitud>) SolicitudRepositorio.findAll();
        List<SolicitudDto> SolicitudDtos = Solicituds.stream().map(Solicitud -> modelMapper.map(Solicitud, SolicitudDto.class)).collect(Collectors.toList());
        return SolicitudDtos;
    }

    public SolicitudDto save( SolicitudDto SolicitudDto){
        validarFechas(SolicitudDto.getFechaInicio(), SolicitudDto.getFechaFin());
        Propiedad propiedad = validarPropiedad(SolicitudDto.getPropiedad().getPropiedadId());
        validarArrendatario(SolicitudDto.getArrendatario().getCedula());
        validarCapacidad(propiedad, SolicitudDto.getCantidadPer());
        SolicitudDto.setCostoTotal(calcularCostoTotal(propiedad, SolicitudDto.getFechaInicio(), SolicitudDto.getFechaFin()));
        Solicitud solicitud = modelMapper.map(SolicitudDto, Solicitud.class);
        solicitud.setStatus(Status.ACTIVE);
        solicitud = SolicitudRepositorio.save(solicitud);
        return modelMapper.map(solicitud, SolicitudDto.class);
    }

    public SolicitudDto update (SolicitudDto SolicitudDto) throws ValidationException{
        SolicitudDto = get(SolicitudDto.getSolicitudId());
        if(SolicitudDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        Solicitud Solicitud = modelMapper.map(SolicitudDto, Solicitud.class);
        Solicitud.setStatus(Status.ACTIVE);
        Solicitud = SolicitudRepositorio.save(Solicitud);
        SolicitudDto = modelMapper.map(Solicitud, SolicitudDto.class);
        return SolicitudDto;
    }

    public void delete (Long id){
        SolicitudRepositorio.deleteById(id);
    }
    private void validarArrendatario(Integer cedula) {
        arrendatarioRepositorio.findById(cedula)
            .orElseThrow(() -> new IllegalArgumentException("Arrendatario no encontrado"));
    }
    private void validarFechas(String fechaInicio, String fechaFin) {
        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);
        
        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        }
    }
    private Propiedad validarPropiedad(Long propiedadId) {
        return propiedadRepositorio.findById(propiedadId)
            .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));
    }
    private int calcularCostoTotal(Propiedad propiedad, String fechaInicio, String fechaFin) {
        long dias = ChronoUnit.DAYS.between(
            LocalDate.parse(fechaInicio),
            LocalDate.parse(fechaFin)
        );
        return propiedad.getValorNoche() * (int) dias;
    }
    private void validarCapacidad(Propiedad propiedad, int cantidadPersonas) {
        if (cantidadPersonas > propiedad.getCapacidad()) {
            throw new IllegalArgumentException("La cantidad de personas excede la capacidad de la propiedad");
        }
}
public List<SolicitudDto> buscarSolicitudes(Long propiedadId, String cedulaArrendatario, String fechaInicio, String fechaFin) {
    Specification<Solicitud> spec = Specification.where(null);
    
    if (propiedadId != null) {
        spec = spec.and((root, query, cb) -> 
            cb.equal(root.get("propiedad").get("propiedad_id"), propiedadId));
    }
    if (cedulaArrendatario != null) {
        spec = spec.and((root, query, cb) -> 
            cb.equal(root.get("arrendatario").get("cedula"), cedulaArrendatario));
    }
    if (fechaInicio != null && fechaFin != null) {
        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);
        spec = spec.and((root, query, cb) -> 
            cb.between(root.get("fechaInicio"), inicio, fin));
    }
    return SolicitudRepositorio.findAll(spec).stream()
        .map(s -> modelMapper.map(s, SolicitudDto.class))
        .collect(Collectors.toList());
}
}
