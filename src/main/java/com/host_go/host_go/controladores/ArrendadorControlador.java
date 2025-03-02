package com.host_go.host_go.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Repositorios.ArrendadorRepositorio;
import com.host_go.host_go.modelos.Arrendador;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "/Arrendador")
public class ArrendadorControlador {

    @Autowired
    private ArrendadorRepositorio arrendadorRepositorio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Arrendador> get () throws Exception{
        return arrendadorRepositorio.findAll();
    }
}
