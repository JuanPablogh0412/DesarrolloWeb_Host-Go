package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.ArrendatarioDto;
import com.host_go.host_go.Repositorios.ArrendatarioRepositorio;
import com.host_go.host_go.modelos.Arrendatario;
import com.host_go.host_go.modelos.Status;

@Service
public class ArrendatarioServicio {

    @Autowired
    ArrendatarioRepositorio ArrendatarioRepositorio;
    @Autowired
    ModelMapper modelMapper;

    public ArrendatarioDto get(Integer id){
        Optional<Arrendatario> ArrendatarioOptional = ArrendatarioRepositorio.findById(id);
        ArrendatarioDto ArrendatarioDto = null;
        if( ArrendatarioOptional != null){
            ArrendatarioDto = modelMapper.map(ArrendatarioOptional.get(), ArrendatarioDto.class);
        }
        return ArrendatarioDto;
    }

    public List<ArrendatarioDto> get( ){
        List<Arrendatario>Arrendatarios = (List<Arrendatario>) ArrendatarioRepositorio.findAll();
        List<ArrendatarioDto> ArrendatarioDtos = Arrendatarios.stream().map(Arrendatario -> modelMapper.map(Arrendatario, ArrendatarioDto.class)).collect(Collectors.toList());
        return ArrendatarioDtos;
    }

    public ArrendatarioDto save( ArrendatarioDto ArrendatarioDto){
        Arrendatario Arrendatario = modelMapper.map(ArrendatarioDto, Arrendatario.class);
        Arrendatario.setStatus(Status.ACTIVE);
        Arrendatario = ArrendatarioRepositorio.save(Arrendatario);
        ArrendatarioDto.setCedula(Arrendatario.getCedula());
        return ArrendatarioDto;
    }

    public ArrendatarioDto update (ArrendatarioDto ArrendatarioDto) throws ValidationException{
        ArrendatarioDto = get(ArrendatarioDto.getCedula());
        if(ArrendatarioDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        Arrendatario Arrendatario = modelMapper.map(ArrendatarioDto, Arrendatario.class);
        Arrendatario.setStatus(Status.ACTIVE);
        Arrendatario = ArrendatarioRepositorio.save(Arrendatario);
        ArrendatarioDto = modelMapper.map(Arrendatario, ArrendatarioDto.class);
        return ArrendatarioDto;
    }

    public void delete (Integer id){
        ArrendatarioRepositorio.deleteById(id);
    }

}
