package com.host_go.host_go.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Repositorios.ArrendatarioRepositorio;
import com.host_go.host_go.modelos.Arrendatario;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "/Arrendatario")
public class ArrendatarioControlador {

    @Autowired
    private ArrendatarioRepositorio ArrendatarioRepositorio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Arrendatario> get () throws Exception{
        return ArrendatarioRepositorio.findAll();
    }
}
