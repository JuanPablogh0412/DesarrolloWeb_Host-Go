package com.host_go.host_go.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Repositorios.FotoRepositorio;
import com.host_go.host_go.modelos.Foto;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "/Foto")
public class FotoControlador {

    @Autowired
    private FotoRepositorio FotoRepositorio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Foto> get () throws Exception{
        return FotoRepositorio.findAll();
    }
}
