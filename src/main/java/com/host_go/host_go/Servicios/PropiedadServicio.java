package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.PropiedadDto;
import com.host_go.host_go.Repositorios.PropiedadRepositorio;
import com.host_go.host_go.modelos.Propiedad;
import com.host_go.host_go.modelos.Status;

@Service
public class PropiedadServicio {

    @Autowired
    PropiedadRepositorio PropiedadRepositorio;
    @Autowired
    ModelMapper modelMapper;

    public PropiedadDto get(Long id){
        Optional<Propiedad> PropiedadOptional = PropiedadRepositorio.findById(id);
        PropiedadDto PropiedadDto = null;
        if( PropiedadOptional != null){
            PropiedadDto = modelMapper.map(PropiedadOptional.get(), PropiedadDto.class);
        }
        return PropiedadDto;
    }

    public List<PropiedadDto> get( ){
        List<Propiedad>Propiedads = (List<Propiedad>) PropiedadRepositorio.findAll();
        List<PropiedadDto> PropiedadDtos = Propiedads.stream().map(Propiedad -> modelMapper.map(Propiedad, PropiedadDto.class)).collect(Collectors.toList());
        return PropiedadDtos;
    }

    public PropiedadDto save( PropiedadDto PropiedadDto){
        Propiedad Propiedad = modelMapper.map(PropiedadDto, Propiedad.class);
        Propiedad.setStatus(Status.ACTIVE);
        Propiedad = PropiedadRepositorio.save(Propiedad);
        PropiedadDto.setPropiedad_id(Propiedad.getPropiedad_id());
        return PropiedadDto;
    }

    public PropiedadDto update (PropiedadDto PropiedadDto) throws ValidationException{
        PropiedadDto = get(PropiedadDto.getPropiedad_id());
        if(PropiedadDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        Propiedad Propiedad = modelMapper.map(PropiedadDto, Propiedad.class);
        Propiedad.setStatus(Status.ACTIVE);
        Propiedad = PropiedadRepositorio.save(Propiedad);
        PropiedadDto = modelMapper.map(Propiedad, PropiedadDto.class);
        return PropiedadDto;
    }

    public void delete (Long id){
        PropiedadRepositorio.deleteById(id);
    }
    
    public List<PropiedadDto> buscarPropiedades(String nombre, String ubicacion, int capacidad) {
        List<Propiedad> propiedades = PropiedadRepositorio
            .findByNombreContainingIgnoreCaseAndUbicacionContainingIgnoreCaseAndCapacidadGreaterThanEqual(
                nombre != null ? nombre : "", 
                ubicacion != null ? ubicacion : "", 
                capacidad
            );
        
        return propiedades.stream()
            .map(propiedad -> modelMapper.map(propiedad, PropiedadDto.class))
            .collect(Collectors.toList());
    }

}
