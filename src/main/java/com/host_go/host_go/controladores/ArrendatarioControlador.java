package com.host_go.host_go.controladores;

import java.util.List;
import java.util.Map;

import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Dtos.ArrendatarioCreateDto;
import com.host_go.host_go.Dtos.ArrendatarioDto;
import com.host_go.host_go.Servicios.ArrendatarioServicio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping(value = "/Arrendatario")
public class ArrendatarioControlador {

    @Autowired
    private ArrendatarioServicio ArrendatarioServicio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArrendatarioDto> get (){
        return ArrendatarioServicio.get();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrendatarioDto get(@PathVariable Long id){
        return ArrendatarioServicio.get(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody ArrendatarioCreateDto arrendatarioCreateDto) {
        try {
            ArrendatarioDto arrendatarioDto = ArrendatarioServicio.save(arrendatarioCreateDto);
            return ResponseEntity.ok(arrendatarioDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", e.getMessage())); // Mensaje de error claro
        } catch (Exception e) {
            return ResponseEntity
                .internalServerError()
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @CrossOrigin
    @PutMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrendatarioDto update(@RequestBody ArrendatarioDto ArrendatarioDto) throws ValidationException{
        return ArrendatarioServicio.update(ArrendatarioDto);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id){
        ArrendatarioServicio.delete(id);
    }
    
}
