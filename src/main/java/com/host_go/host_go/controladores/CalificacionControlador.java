package com.host_go.host_go.controladores;

import java.util.List;

import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Dtos.CalificacionDto;
import com.host_go.host_go.Servicios.CalificacionServicio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping(value = "/Calificacion")
public class CalificacionControlador {

    @Autowired
    private CalificacionServicio CalificacionServicio;

    

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CalificacionDto> get (){
        return CalificacionServicio.get();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CalificacionDto get(@PathVariable Long id){
        return CalificacionServicio.get(id);
    }
    //BackEnd comentarios arrendadores y arrendatarios
    @CrossOrigin
    @PostMapping(value = "/calificar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calificarUsuario(
        @RequestParam int estrellas,
        @RequestParam String comentario,
        @RequestParam String usuario) {

        if (estrellas <= 0 || comentario.isEmpty() || usuario.isEmpty()) {
            return ResponseEntity.badRequest().body("Todos los espacios son obligatorios");
        }
        return CalificacionServicio.calificarUsuario(estrellas, comentario, usuario);
    }
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CalificacionDto save(@RequestBody CalificacionDto CalificacionDto) throws ValidationException{
        return CalificacionServicio.save(CalificacionDto);
    }

    @CrossOrigin
    @PutMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public CalificacionDto update(@RequestBody CalificacionDto CalificacionDto) throws ValidationException{
        return CalificacionServicio.update(CalificacionDto);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id){
        CalificacionServicio.delete(id);
    }
    
}
