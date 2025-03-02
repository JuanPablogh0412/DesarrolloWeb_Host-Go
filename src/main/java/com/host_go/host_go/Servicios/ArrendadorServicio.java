package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.ArrendadorDto;
import com.host_go.host_go.Repositorios.ArrendadorRepositorio;
import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Status;

@Service
public class ArrendadorServicio {

    @Autowired
    ArrendadorRepositorio arrendadorRepositorio;
    @Autowired
    ModelMapper modelMapper;

    public ArrendadorDto get(Integer id){
        Optional<Arrendador> arrendadorOptional = arrendadorRepositorio.findById(id);
        ArrendadorDto arrendadorDto = null;
        if( arrendadorOptional != null){
            arrendadorDto = modelMapper.map(arrendadorOptional.get(), ArrendadorDto.class);
        }
        return arrendadorDto;
    }

    public List<ArrendadorDto> get( ){
        List<Arrendador>arrendadors = (List<Arrendador>) arrendadorRepositorio.findAll();
        List<ArrendadorDto> arrendadorDtos = arrendadors.stream().map(arrendador -> modelMapper.map(arrendador, ArrendadorDto.class)).collect(Collectors.toList());
        return arrendadorDtos;
    }

    public ArrendadorDto save( ArrendadorDto arrendadorDto){
        Arrendador arrendador = modelMapper.map(arrendadorDto, Arrendador.class);
        arrendador.setStatus(Status.ACTIVE);
        arrendador = arrendadorRepositorio.save(arrendador);
        arrendadorDto.setCedula(arrendador.getCedula());
        return arrendadorDto;
    }

    public ArrendadorDto update (ArrendadorDto arrendadorDto) throws ValidationException{
        arrendadorDto = get(arrendadorDto.getCedula());
        if(arrendadorDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        Arrendador arrendador = modelMapper.map(arrendadorDto, Arrendador.class);
        arrendador.setStatus(Status.ACTIVE);
        arrendador = arrendadorRepositorio.save(arrendador);
        arrendadorDto = modelMapper.map(arrendador, ArrendadorDto.class);
        return arrendadorDto;
    }

    public void delete (Integer id){
        arrendadorRepositorio.deleteById(id);
    }

}
