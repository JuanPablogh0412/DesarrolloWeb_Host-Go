package com.host_go.host_go.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Repositorios.SolicitudRepositorio;
import com.host_go.host_go.modelos.Solicitud;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "/Solicitud")
public class SolicitudControlador {

    @Autowired
    private SolicitudRepositorio SolicitudRepositorio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Solicitud> get () throws Exception{
        return SolicitudRepositorio.findAll();
    }
}
