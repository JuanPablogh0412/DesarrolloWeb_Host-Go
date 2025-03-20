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

import com.host_go.host_go.Dtos.SolicitudDto;
import com.host_go.host_go.Servicios.SolicitudServicio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping(value = "/Solicitud")
public class SolicitudControlador {

    @Autowired
    private SolicitudServicio SolicitudServicio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SolicitudDto> get (){
        return SolicitudServicio.get();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SolicitudDto get(@PathVariable Long id){
        return SolicitudServicio.get(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SolicitudDto save(@RequestBody SolicitudDto SolicitudDto) throws ValidationException{
        return SolicitudServicio.save(SolicitudDto);
    }

    @CrossOrigin
    @PutMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public SolicitudDto update(@RequestBody SolicitudDto SolicitudDto) throws ValidationException{
        return SolicitudServicio.update(SolicitudDto);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id){
        SolicitudServicio.delete(id);
    }
    @CrossOrigin
    @GetMapping(value = "/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SolicitudDto> buscarSolicitudes(
    @RequestParam(required = false) Long propiedadId,
    @RequestParam(required = false) String cedulaArrendatario,
    @RequestParam(required = false) String fechaInicio,
    @RequestParam(required = false) String fechaFin
) {
    return SolicitudServicio.buscarSolicitudes(propiedadId, cedulaArrendatario, fechaInicio, fechaFin);
}
    
}
