package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.host_go.host_go.Dtos.PropiedadDto;
import com.host_go.host_go.Repositorios.PropiedadRepositorio;
import com.host_go.host_go.modelos.Propiedad;

@Service
public class PropiedadServicio {

    @Autowired
    private PropiedadRepositorio propiedadRepositorio;
    
    @Autowired
    private ModelMapper modelMapper;
    
    private static final String DANE_API_URL = "https://www.datos.gov.co/resource/xdk5-pm3f.json";

    public PropiedadDto get(Long id) {
        Optional<Propiedad> propiedadOptional = propiedadRepositorio.findById(id);
        return propiedadOptional.map(propiedad -> modelMapper.map(propiedad, PropiedadDto.class)).orElse(null);
    }

    public List<PropiedadDto> get() {
        List<Propiedad> propiedades = propiedadRepositorio.findAll()
                .stream()
                .filter(p -> p.getEstado() == 1) // Solo mostramos las activas
                .collect(Collectors.toList());
        
        return propiedades.stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadDto.class))
                .collect(Collectors.toList());
    }

    // HUA.5 - Crear Propiedad con validaciones
    public PropiedadDto save(PropiedadDto propiedadDto) {
        validarDatos(propiedadDto);

        Propiedad propiedad = modelMapper.map(propiedadDto, Propiedad.class);
        propiedad.setEstado(1);
        propiedad = propiedadRepositorio.save(propiedad);
        propiedadDto.setPropiedad_id(propiedad.getPropiedad_id());

        return propiedadDto;
    }

    public PropiedadDto update(PropiedadDto propiedadDto) {
        if (propiedadDto == null || propiedadDto.getPropiedad_id() == 0) {
            throw new IllegalArgumentException("Propiedad no encontrada");
        }
        Propiedad propiedad = modelMapper.map(propiedadDto, Propiedad.class);
        propiedad = propiedadRepositorio.save(propiedad);
        return modelMapper.map(propiedad, PropiedadDto.class);
    }

    // HUA.8 - Desactivar Propiedad
    public void desactivarPropiedad(Long id) {
        Optional<Propiedad> propiedadOptional = propiedadRepositorio.findById(id);
        if (propiedadOptional.isPresent()) {
            Propiedad propiedad = propiedadOptional.get();
            propiedad.setEstado(0);
            propiedadRepositorio.save(propiedad);
        } else {
            throw new IllegalArgumentException("Propiedad no encontrada");
        }
    }

    public void delete(Long id) {
        propiedadRepositorio.deleteById(id);
    }

    // Validaciones
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
    }

    private boolean esDepartamentoMunicipioValido(String departamento, String municipio) {
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(DANE_API_URL, String.class);
        return json.contains(departamento) && json.contains(municipio);
    }
}
