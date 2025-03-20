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

import com.host_go.host_go.Dtos.ArrendadorCreateDto;
import com.host_go.host_go.Dtos.ArrendadorDto;
import com.host_go.host_go.Servicios.ArrendadorServicio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = "/Arrendador")
public class ArrendadorControlador {

    @Autowired
    private ArrendadorServicio arrendadorServicio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArrendadorDto> get (){
        return arrendadorServicio.get();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrendadorDto get(@PathVariable Integer id){
        return arrendadorServicio.get(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody ArrendadorCreateDto arrendadorCreateDto) {
        try {
            ArrendadorDto arrendadorDto = arrendadorServicio.save(arrendadorCreateDto);
            return ResponseEntity.ok(arrendadorDto);
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
    public ArrendadorDto update(@RequestBody ArrendadorDto arrendadorDto) throws ValidationException{
        return arrendadorServicio.update(arrendadorDto);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Integer id){
        arrendadorServicio.delete(id);
    }
    
}
