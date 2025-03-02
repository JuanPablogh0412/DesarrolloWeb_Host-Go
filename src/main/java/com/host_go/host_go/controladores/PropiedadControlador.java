package com.host_go.host_go.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Repositorios.PropiedadRepositorio;
import com.host_go.host_go.modelos.Propiedad;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "/Propiedad")
public class PropiedadControlador {

    @Autowired
    private PropiedadRepositorio PropiedadRepositorio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Propiedad> get () throws Exception{
        return PropiedadRepositorio.findAll();
    }
}
