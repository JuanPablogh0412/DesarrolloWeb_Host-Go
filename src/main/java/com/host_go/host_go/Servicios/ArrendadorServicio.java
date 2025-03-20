package com.host_go.host_go.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.ArrendadorCreateDto;
import com.host_go.host_go.Dtos.ArrendadorDto;
import com.host_go.host_go.Repositorios.ArrendadorRepositorio;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Cuenta;
import com.host_go.host_go.modelos.Status;

@Service
public class ArrendadorServicio {

    @Autowired
    ArrendadorRepositorio arrendadorRepositorio;
    @Autowired
    private CuentaRepositorio cuentaRepositorio;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder; 

    public ArrendadorDto get(Integer id){
        Optional<Arrendador> arrendadorOptional = arrendadorRepositorio.findById(id);
        ArrendadorDto arrendadorDto = null;
        if( arrendadorOptional != null){
            arrendadorDto = modelMapper.map(arrendadorOptional.get(), ArrendadorDto.class);
        }
        return arrendadorDto;
    }

    public List<ArrendadorDto> get( ){
        List<Arrendador>arrendadors = (List<Arrendador>) arrendadorRepositorio.findAll();
        List<ArrendadorDto> arrendadorDtos = arrendadors.stream().map(arrendador -> modelMapper.map(arrendador, ArrendadorDto.class)).collect(Collectors.toList());
        return arrendadorDtos;
    }

    public ArrendadorDto save(ArrendadorCreateDto arrendadorCreateDto) {
        // Validar correo único
        if (arrendadorRepositorio.existsByCorreo(arrendadorCreateDto.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // Validar formato de correo corporativo
        if (!arrendadorCreateDto.getCorreo().endsWith("@javeriana.edu.co")) {
            throw new IllegalArgumentException("Correo no cumple con el formato corporativo");
        }

        // Validar contraseña
        if (arrendadorCreateDto.getContrasena().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }

        // Crear la cuenta asociada
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(arrendadorCreateDto.getCorreo());
        cuenta.setContrasena(passwordEncoder.encode(arrendadorCreateDto.getContrasena()));
        cuenta.setStatus(Status.ACTIVE);
        cuenta = cuentaRepositorio.save(cuenta);

        // Crear el arrendador
        Arrendador arrendador = modelMapper.map(arrendadorCreateDto, Arrendador.class);
        arrendador.setCuenta(cuenta);
        arrendador.setStatus(Status.ACTIVE);
        arrendador = arrendadorRepositorio.save(arrendador);

        // Convertir a DTO de respuesta (sin contraseña)
        ArrendadorDto arrendadorDto = modelMapper.map(arrendador, ArrendadorDto.class);
        return arrendadorDto;
    }

    public ArrendadorDto update (ArrendadorDto arrendadorDto) throws ValidationException{
        arrendadorDto = get(arrendadorDto.getCedula());
        if(arrendadorDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        Arrendador arrendador = modelMapper.map(arrendadorDto, Arrendador.class);
        arrendador.setStatus(Status.ACTIVE);
        arrendador = arrendadorRepositorio.save(arrendador);
        arrendadorDto = modelMapper.map(arrendador, ArrendadorDto.class);
        return arrendadorDto;
    }

    public void delete (Integer id){
        arrendadorRepositorio.deleteById(id);
    }

}
