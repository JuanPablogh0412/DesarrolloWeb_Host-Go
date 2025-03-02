package com.host_go.host_go.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Repositorios.CuentaRepositorio;
import com.host_go.host_go.modelos.Cuenta;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "/Cuenta")
public class CuentaControlador {

    @Autowired
    private CuentaRepositorio CuentaRepositorio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Cuenta> get () throws Exception{
        return CuentaRepositorio.findAll();
    }
}
