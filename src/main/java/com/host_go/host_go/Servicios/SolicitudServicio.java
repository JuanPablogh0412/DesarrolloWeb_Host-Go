package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.SolicitudDto;
import com.host_go.host_go.Repositorios.SolicitudRepositorio;
import com.host_go.host_go.modelos.Solicitud;
import com.host_go.host_go.modelos.Status;

@Service
public class SolicitudServicio {

    @Autowired
    SolicitudRepositorio SolicitudRepositorio;
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
        Solicitud Solicitud = modelMapper.map(SolicitudDto, Solicitud.class);
        Solicitud.setStatus(Status.ACTIVE);
        Solicitud = SolicitudRepositorio.save(Solicitud);
        SolicitudDto.setSolicitud_id(Solicitud.getSolicitud_id());
        return SolicitudDto;
    }

    public SolicitudDto update (SolicitudDto SolicitudDto) throws ValidationException{
        SolicitudDto = get(SolicitudDto.getSolicitud_id());
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

}
