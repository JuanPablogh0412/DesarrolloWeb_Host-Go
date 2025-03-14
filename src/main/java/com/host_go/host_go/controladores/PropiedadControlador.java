package com.host_go.host_go.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.host_go.host_go.Dtos.PropiedadDto;
import com.host_go.host_go.Servicios.PropiedadServicio;

@RestController
@RequestMapping(value = "/Propiedad")
public class PropiedadControlador {

    @Autowired
    private PropiedadServicio propiedadServicio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PropiedadDto> get() {
        return propiedadServicio.get();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PropiedadDto get(@PathVariable Long id) {
        return propiedadServicio.get(id);
    }

    // HUA.5 - Crear propiedad
    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PropiedadDto save(@RequestBody PropiedadDto propiedadDto) {
        return propiedadServicio.save(propiedadDto);
    }

    @CrossOrigin
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PropiedadDto update(@RequestBody PropiedadDto propiedadDto) {
        return propiedadServicio.update(propiedadDto);
    }

    // HUA.8 - Desactivar propiedad
    @CrossOrigin
    @PutMapping(value = "/desactivar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void desactivarPropiedad(@PathVariable Long id) {
        propiedadServicio.desactivarPropiedad(id);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id) {
        propiedadServicio.delete(id);
    }
}
