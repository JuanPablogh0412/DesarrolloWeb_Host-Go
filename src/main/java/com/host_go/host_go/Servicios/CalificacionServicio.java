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
import com.host_go.host_go.modelos.Calificacion;
import com.host_go.host_go.modelos.Status;

@Service
public class CalificacionServicio {

    @Autowired
    CalificacionRepositorio CalificacionRepositorio;
    @Autowired
    ModelMapper modelMapper;

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

    public CalificacionDto save( CalificacionDto CalificacionDto){
        Calificacion Calificacion = modelMapper.map(CalificacionDto, Calificacion.class);
        Calificacion.setStatus(Status.ACTIVE);
        Calificacion = CalificacionRepositorio.save(Calificacion);
        CalificacionDto.setCalificacion_id(Calificacion.getCalificacion_id());
        return CalificacionDto;
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
