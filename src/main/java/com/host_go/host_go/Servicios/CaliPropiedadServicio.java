package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.CaliPropiedadDto;
import com.host_go.host_go.Repositorios.CaliPropiedadRepositorio;
import com.host_go.host_go.modelos.CaliPropiedad;
import com.host_go.host_go.modelos.Status;

@Service
public class CaliPropiedadServicio {

    @Autowired
    CaliPropiedadRepositorio CaliPropiedadRepositorio;
    @Autowired
    ModelMapper modelMapper;

    public CaliPropiedadDto get(Long id){
        Optional<CaliPropiedad> CaliPropiedadOptional = CaliPropiedadRepositorio.findById(id);
        CaliPropiedadDto CaliPropiedadDto = null;
        if( CaliPropiedadOptional != null){
            CaliPropiedadDto = modelMapper.map(CaliPropiedadOptional.get(), CaliPropiedadDto.class);
        }
        return CaliPropiedadDto;
    }

    public List<CaliPropiedadDto> get( ){
        List<CaliPropiedad>CaliPropiedads = (List<CaliPropiedad>) CaliPropiedadRepositorio.findAll();
        List<CaliPropiedadDto> CaliPropiedadDtos = CaliPropiedads.stream().map(CaliPropiedad -> modelMapper.map(CaliPropiedad, CaliPropiedadDto.class)).collect(Collectors.toList());
        return CaliPropiedadDtos;
    }

    public CaliPropiedadDto save( CaliPropiedadDto CaliPropiedadDto){
        CaliPropiedad CaliPropiedad = modelMapper.map(CaliPropiedadDto, CaliPropiedad.class);
        CaliPropiedad.setStatus(Status.ACTIVE);
        CaliPropiedad = CaliPropiedadRepositorio.save(CaliPropiedad);
        CaliPropiedadDto.setCaliPropiedadId(CaliPropiedad.getCaliPropiedadId());
        return CaliPropiedadDto;
    }

    public CaliPropiedadDto update (CaliPropiedadDto CaliPropiedadDto) throws ValidationException{
        CaliPropiedadDto = get(CaliPropiedadDto.getCaliPropiedadId());
        if(CaliPropiedadDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        CaliPropiedad CaliPropiedad = modelMapper.map(CaliPropiedadDto, CaliPropiedad.class);
        CaliPropiedad.setStatus(Status.ACTIVE);
        CaliPropiedad = CaliPropiedadRepositorio.save(CaliPropiedad);
        CaliPropiedadDto = modelMapper.map(CaliPropiedad, CaliPropiedadDto.class);
        return CaliPropiedadDto;
    }

    public void delete (Long id){
        CaliPropiedadRepositorio.deleteById(id);
    }

}
