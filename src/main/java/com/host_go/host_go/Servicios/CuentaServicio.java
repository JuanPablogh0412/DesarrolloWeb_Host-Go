package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.CuentaDto;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import com.host_go.host_go.modelos.Cuenta;
import com.host_go.host_go.modelos.Status;

@Service
public class CuentaServicio {

    @Autowired
    CuentaRepositorio CuentaRepositorio;
    @Autowired
    ModelMapper modelMapper;

    public CuentaDto get(Long id){
        Optional<Cuenta> CuentaOptional = CuentaRepositorio.findById(id);
        CuentaDto CuentaDto = null;
        if( CuentaOptional != null){
            CuentaDto = modelMapper.map(CuentaOptional.get(), CuentaDto.class);
        }
        return CuentaDto;
    }

    public List<CuentaDto> get( ){
        List<Cuenta>Cuentas = (List<Cuenta>) CuentaRepositorio.findAll();
        List<CuentaDto> CuentaDtos = Cuentas.stream().map(Cuenta -> modelMapper.map(Cuenta, CuentaDto.class)).collect(Collectors.toList());
        return CuentaDtos;
    }

    public CuentaDto save( CuentaDto CuentaDto){
        Cuenta Cuenta = modelMapper.map(CuentaDto, Cuenta.class);
        Cuenta.setStatus(Status.ACTIVE);
        Cuenta = CuentaRepositorio.save(Cuenta);
        CuentaDto.setCuenta_id(Cuenta.getCuenta_id());
        return CuentaDto;
    }

    public CuentaDto update (CuentaDto CuentaDto) throws ValidationException{
        CuentaDto = get(CuentaDto.getCuenta_id());
        if(CuentaDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        Cuenta Cuenta = modelMapper.map(CuentaDto, Cuenta.class);
        Cuenta.setStatus(Status.ACTIVE);
        Cuenta = CuentaRepositorio.save(Cuenta);
        CuentaDto = modelMapper.map(Cuenta, CuentaDto.class);
        return CuentaDto;
    }

    public void delete (Long id){
        CuentaRepositorio.deleteById(id);
    }

}
