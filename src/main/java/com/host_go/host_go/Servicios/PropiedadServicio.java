package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.host_go.host_go.Dtos.PropiedadDto;
import com.host_go.host_go.Repositorios.PropiedadRepositorio;
import com.host_go.host_go.Repositorios.ArrendadorRepositorio;

import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Propiedad;
import com.host_go.host_go.modelos.Status;

@Service
public class PropiedadServicio {

    @Autowired
    PropiedadRepositorio PropiedadRepositorio;
    @Autowired
    ArrendadorRepositorio arrendadorRepositorio;
    @Autowired
    ModelMapper modelMapper;
    private static final String DANE_API_URL = "https://www.datos.gov.co/resource/xdk5-pm3f.json";

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

    public PropiedadDto save(PropiedadDto propiedadDto) {
        validarDatos(propiedadDto);
    
        Propiedad propiedad = modelMapper.map(propiedadDto, Propiedad.class);
    
        Arrendador arrendador = arrendadorRepositorio.findByArrendadorId(propiedadDto.getArrendador().getArrendadorId())
            .orElseThrow(() -> new IllegalArgumentException("Arrendador no encontrado"));
        propiedad.setArrendador(arrendador);
    
        propiedad.setStatus(Status.ACTIVE);
    
        propiedad = PropiedadRepositorio.save(propiedad);
    
        PropiedadDto savedPropiedadDto = modelMapper.map(propiedad, PropiedadDto.class);
    
        return savedPropiedadDto;
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
    
    public List<PropiedadDto> buscarPropiedades(String nombre, String municipio, int capacidad) {
        List<Propiedad> propiedades = PropiedadRepositorio
            .findByNombreContainingIgnoreCaseAndMunicipioContainingIgnoreCaseAndCapacidadGreaterThanEqual(
                nombre != null ? nombre : "", 
                municipio != null ? municipio : "", 
                capacidad
            );
        
        return propiedades.stream()
            .map(propiedad -> modelMapper.map(propiedad, PropiedadDto.class))
            .collect(Collectors.toList());
    }

    private void validarDatos(PropiedadDto propiedadDto) {
        if (propiedadDto.getNombre() == null || propiedadDto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (!esDepartamentoMunicipioValido(propiedadDto.getDepartamento(), propiedadDto.getMunicipio())) {
            throw new IllegalArgumentException("Departamento/Municipio no válido según DANE.");
        }
        if (propiedadDto.getHabitaciones() <= 0) {
            throw new IllegalArgumentException("Debe haber al menos una habitación.");
        }
        if (propiedadDto.getBanos() <= 0) {
            throw new IllegalArgumentException("Debe haber al menos un baño.");
        }
        if (arrendadorRepositorio.findByArrendadorId(propiedadDto.getArrendador().getArrendadorId()) == null){
            throw new IllegalArgumentException("No existe un arrendador");
        }
    }

    private boolean esDepartamentoMunicipioValido(String departamento, String municipio) {
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(DANE_API_URL, String.class);
        return json.contains(departamento) && json.contains(municipio);
    }

}
