package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.CaliPropiedadDto;
import com.host_go.host_go.Repositorios.CaliPropiedadRepositorio;
import com.host_go.host_go.modelos.CaliPropiedad;
import com.host_go.host_go.modelos.Status;
import com.host_go.host_go.modelos.Propiedad;

@Service
public class CaliPropiedadServicio {

    

    @Autowired
    private PropiedadServicio propiedadServicio;
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
        CaliPropiedadDto.setCaliPropiedad_id(CaliPropiedad.getCaliPropiedad_id());
        return CaliPropiedadDto;
    }

    public CaliPropiedadDto update (CaliPropiedadDto CaliPropiedadDto) throws ValidationException{
        CaliPropiedadDto = get(CaliPropiedadDto.getCaliPropiedad_id());
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

    public ResponseEntity<?> calificarPropiedad(int estrellas, String comentario, String nombrePropiedad, String ubicacion){
        Optional<Propiedad> propiedadOpt = propiedadServicio.buscarPorNombreAndUbicacion(nombrePropiedad, ubicacion);
        if(propiedadOpt.isEmpty()){
            return ResponseEntity.badRequest().body("no se encontro la propiedad");
        }
        CaliPropiedad calificacion = new CaliPropiedad();
        calificacion.setEstrellas(estrellas);
        calificacion.setComentario(comentario);
        calificacion.setPropiedad(propiedadOpt.get());
        CaliPropiedadRepositorio.save(calificacion);
        return ResponseEntity.ok("Calificacion guardada correctamente");
    }
    public List<CaliPropiedadDto> obtenerComentariosporPropiedad(String nombre, String ubicacion){
        Optional<Propiedad> propiedadOpt = propiedadServicio.buscarPorNombreAndUbicacion(nombre, ubicacion);
        if (propiedadOpt.isEmpty()) {
            return List.of();
        }
        List<CaliPropiedad> comentarios = CaliPropiedadRepositorio.findByPropiedad(propiedadOpt.get());
        return comentarios.stream()
            .map(c -> modelMapper.map(c, CaliPropiedadDto.class))
            .collect(Collectors.toList());
    }

}
