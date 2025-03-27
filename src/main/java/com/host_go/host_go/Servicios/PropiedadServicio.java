package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.host_go.host_go.Dtos.PropiedadDto;
import com.host_go.host_go.Dtos.PropiedadResumenDto;
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

    public PropiedadDto update(PropiedadDto propiedadDto) {
        Propiedad propiedadExistente = PropiedadRepositorio.findById(propiedadDto.getPropiedadId())
            .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));
    
        validarDatosBasicos(propiedadDto);
    
        propiedadExistente.setNombre(propiedadDto.getNombre());
        propiedadExistente.setDepartamento(propiedadDto.getDepartamento());
        propiedadExistente.setMunicipio(propiedadDto.getMunicipio());
        propiedadExistente.setTipoIngreso(propiedadDto.getTipoIngreso());
        propiedadExistente.setDescripcion(propiedadDto.getDescripcion());
        propiedadExistente.setCapacidad(propiedadDto.getCapacidad());
        propiedadExistente.setHabitaciones(propiedadDto.getHabitaciones());
        propiedadExistente.setBanos(propiedadDto.getBanos());
        propiedadExistente.setPermiteMascotas(propiedadDto.isPermiteMascotas());
        propiedadExistente.setTienePiscina(propiedadDto.isTienePiscina());
        propiedadExistente.setTieneAsador(propiedadDto.isTieneAsador());
        propiedadExistente.setValorNoche(propiedadDto.getValorNoche());
    
        Propiedad propiedadActualizada = PropiedadRepositorio.save(propiedadExistente);
        
        return modelMapper.map(propiedadActualizada, PropiedadDto.class);
    }
    
    // Método de validación simplificado (sin validar arrendador)
    private void validarDatosBasicos(PropiedadDto propiedadDto) {
        if (propiedadDto.getNombre() == null || propiedadDto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        // ... (otras validaciones excepto arrendador)
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
        if (propiedadDto.getDepartamento() == null || propiedadDto.getMunicipio() == null) {
            throw new IllegalArgumentException("Departamento y municipio son obligatorios.");
        }
        if (!esDepartamentoMunicipioValido(propiedadDto.getDepartamento(), propiedadDto.getMunicipio())) {
            throw new IllegalArgumentException("Departamento/Municipio no válido según DANE.");
        }
        if (propiedadDto.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0.");
        }
        if (propiedadDto.getHabitaciones() <= 0) {
            throw new IllegalArgumentException("Debe haber al menos una habitación.");
        }
        if (propiedadDto.getBanos() <= 0) {
            throw new IllegalArgumentException("Debe haberSolicitudDto al menos un baño.");
        }
        if (propiedadDto.getValorNoche() <= 0) {
            throw new IllegalArgumentException("El valor por noche debe ser mayor a 0.");
        }
    }

    private boolean esDepartamentoMunicipioValido(String departamento, String municipio) {
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(DANE_API_URL, String.class);
        return json.contains(departamento) && json.contains(municipio);
    }


    public List<PropiedadResumenDto> obtenerPropiedadesPorArrendador(Long arrendadorId) {
    if (!arrendadorRepositorio.existsByArrendadorId(arrendadorId)) {
        throw new IllegalArgumentException("Arrendador no encontrado");
    }
    
    List<Propiedad> propiedades = PropiedadRepositorio.findByArrendadorArrendadorId(arrendadorId);
    
    return propiedades.stream()
        .map(PropiedadResumenDto::new)
        .collect(Collectors.toList());
    }


    public PropiedadDto desactivarPropiedad(Long propiedadId){
        Propiedad propiedadExistente = PropiedadRepositorio.findById(propiedadId)
        .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));
        propiedadExistente.setStatus(Status.INACTIVE);
        Propiedad propiedadActualizada = PropiedadRepositorio.save(propiedadExistente);
        return modelMapper.map(propiedadActualizada, PropiedadDto.class);

    }

}
