package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.CalificacionDto;
import com.host_go.host_go.Repositorios.CalificacionRepositorio;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import com.host_go.host_go.modelos.Calificacion;
import com.host_go.host_go.modelos.Cuenta;
import com.host_go.host_go.modelos.Status;

@Service
public class CalificacionServicio {


    @Autowired
    CalificacionRepositorio CalificacionRepositorio;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    public CalificacionDto get(Long id){
        Optional<Calificacion> CalificacionOptional = CalificacionRepositorio.findById(id);
        CalificacionDto CalificacionDto = null;
        if( CalificacionOptional != null){
            CalificacionDto = modelMapper.map(CalificacionOptional.get(), CalificacionDto.class);
        }
        return CalificacionDto;
    }

    public List<CalificacionDto> get( ){
        List<Calificacion>Calificacions = (List<Calificacion>) CalificacionRepositorio.findAll();
        List<CalificacionDto> CalificacionDtos = Calificacions.stream().map(Calificacion -> modelMapper.map(Calificacion, CalificacionDto.class)).collect(Collectors.toList());
        return CalificacionDtos;
    }

    public CalificacionDto save(CalificacionDto calificacionDto) {
        // Validar estrellas
        if (calificacionDto.getEstrellas() < 1 || calificacionDto.getEstrellas() > 5) {
            throw new IllegalArgumentException("La calificación debe ser entre 1 y 5 estrellas");
        }

        // Obtener la cuenta desde la base de datos
        Cuenta cuenta = cuentaRepositorio.findById(calificacionDto.getCuentaId())
            .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        // Mapear DTO a entidad
        Calificacion calificacion = modelMapper.map(calificacionDto, Calificacion.class);
        calificacion.setCuenta(cuenta); // Asignar la cuenta obtenida
        calificacion.setStatus(Status.ACTIVE);

        // Guardar la calificación
        calificacion = CalificacionRepositorio.save(calificacion);

        // Retornar DTO actualizado
        return modelMapper.map(calificacion, CalificacionDto.class);
    }

    public CalificacionDto update (CalificacionDto CalificacionDto) throws ValidationException{
        CalificacionDto = get(CalificacionDto.getCalificacion_id());
        if(CalificacionDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        Calificacion Calificacion = modelMapper.map(CalificacionDto, Calificacion.class);
        Calificacion.setStatus(Status.ACTIVE);
        Calificacion = CalificacionRepositorio.save(Calificacion);
        CalificacionDto = modelMapper.map(Calificacion, CalificacionDto.class);
        return CalificacionDto;
    }

    public void delete (Long id){
        CalificacionRepositorio.deleteById(id);
    }

}
