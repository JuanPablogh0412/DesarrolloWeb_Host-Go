package com.host_go.host_go.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Repositorios.CalificacionRepositorio;
import com.host_go.host_go.modelos.Calificacion;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "/Calificacion")
public class CalificacionControlador {

    @Autowired
    private CalificacionRepositorio CalificacionRepositorio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Calificacion> get () throws Exception{
        return CalificacionRepositorio.findAll();
    }
}
