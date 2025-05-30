package com.host_go.host_go.controladores;

import java.util.List;

import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Dtos.PropiedadDto;
import com.host_go.host_go.Servicios.PropiedadServicio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping(value = "/Propiedad")
public class PropiedadControlador {

    @Autowired
    private PropiedadServicio PropiedadServicio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PropiedadDto> get (){
        return PropiedadServicio.get();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PropiedadDto get(@PathVariable Long id){
        return PropiedadServicio.get(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PropiedadDto save(@RequestBody PropiedadDto PropiedadDto) throws ValidationException{
        return PropiedadServicio.save(PropiedadDto);
    }

    @CrossOrigin
    @PutMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public PropiedadDto update(@RequestBody PropiedadDto PropiedadDto) throws ValidationException{
        return PropiedadServicio.update(PropiedadDto);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id){
        PropiedadServicio.delete(id);
    }

    @GetMapping(value = "/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PropiedadDto> buscarPropiedades(
     @RequestParam(required = false) String nombre,
     @RequestParam(required = false) String ubicacion,
     @RequestParam(defaultValue = "0") int capacidad
) {
    return PropiedadServicio.buscarPropiedades(nombre, ubicacion, capacidad);
}
    
}
