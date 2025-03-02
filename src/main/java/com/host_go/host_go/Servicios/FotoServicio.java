package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.FotoDto;
import com.host_go.host_go.Repositorios.FotoRepositorio;
import com.host_go.host_go.modelos.Foto;
import com.host_go.host_go.modelos.Status;

@Service
public class FotoServicio {

    @Autowired
    FotoRepositorio FotoRepositorio;
    @Autowired
    ModelMapper modelMapper;

    public FotoDto get(Long id){
        Optional<Foto> FotoOptional = FotoRepositorio.findById(id);
        FotoDto FotoDto = null;
        if( FotoOptional != null){
            FotoDto = modelMapper.map(FotoOptional.get(), FotoDto.class);
        }
        return FotoDto;
    }

    public List<FotoDto> get( ){
        List<Foto>Fotos = (List<Foto>) FotoRepositorio.findAll();
        List<FotoDto> FotoDtos = Fotos.stream().map(Foto -> modelMapper.map(Foto, FotoDto.class)).collect(Collectors.toList());
        return FotoDtos;
    }

    public FotoDto save( FotoDto FotoDto){
        Foto Foto = modelMapper.map(FotoDto, Foto.class);
        Foto.setStatus(Status.ACTIVE);
        Foto = FotoRepositorio.save(Foto);
        FotoDto.setFoto_id(Foto.getFoto_id());
        return FotoDto;
    }

    public FotoDto update (FotoDto FotoDto) throws ValidationException{
        FotoDto = get(FotoDto.getFoto_id());
        if(FotoDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        Foto Foto = modelMapper.map(FotoDto, Foto.class);
        Foto.setStatus(Status.ACTIVE);
        Foto = FotoRepositorio.save(Foto);
        FotoDto = modelMapper.map(Foto, FotoDto.class);
        return FotoDto;
    }

    public void delete (Long id){
        FotoRepositorio.deleteById(id);
    }

}
