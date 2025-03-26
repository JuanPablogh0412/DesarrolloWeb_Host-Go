package com.host_go.host_go.Servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.ArrendadorCreateDto;
import com.host_go.host_go.Dtos.ArrendadorDto;
import com.host_go.host_go.Repositorios.ActivationTokenRepositorio;
import com.host_go.host_go.Repositorios.ArrendadorRepositorio;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import com.host_go.host_go.modelos.ActivationToken;
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
    @Autowired
    private ActivationTokenRepositorio activationTokenRepositorio;
    @Autowired
    private EmailServicio emailService;

    public ArrendadorDto get(Long id){
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
        cuenta.setTipo("ARRENDADOR");
        cuenta.setStatus(Status.INACTIVE);
        cuenta = cuentaRepositorio.save(cuenta);
        // Generar token de activación
        ActivationToken activationToken = createActivationToken(cuenta);
        
        // Enviar correo
        String activationLink = "http://localhost:8080/auth/activate?token=" + activationToken.getToken();
        emailService.sendActivationEmail(cuenta.getUsuario(), activationLink);

        // Crear el arrendador
        Arrendador arrendador = modelMapper.map(arrendadorCreateDto, Arrendador.class);
        arrendador.setCuenta(cuenta);
        arrendador.setStatus(Status.INACTIVE);
        arrendador = arrendadorRepositorio.save(arrendador);

        // Convertir a DTO de respuesta (sin contraseña)
        ArrendadorDto arrendadorDto = modelMapper.map(arrendador, ArrendadorDto.class);
        return arrendadorDto;
    }

    public ArrendadorDto update (ArrendadorDto arrendadorDto) throws ValidationException{
        arrendadorDto = get(arrendadorDto.getArrendadorId());
        if(arrendadorDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        Arrendador arrendador = modelMapper.map(arrendadorDto, Arrendador.class);
        arrendador.setStatus(Status.ACTIVE);
        arrendador = arrendadorRepositorio.save(arrendador);
        arrendadorDto = modelMapper.map(arrendador, ArrendadorDto.class);
        return arrendadorDto;
    }

    public void delete (Long id){
        arrendadorRepositorio.deleteById(id);
    }

    private ActivationToken createActivationToken(Cuenta cuenta) {
        ActivationToken token = new ActivationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpirationDate(LocalDateTime.now().plusHours(24));
        token.setCuenta(cuenta);
        return activationTokenRepositorio.save(token);
    }

    public Arrendador validarCredenciales(String correo, String contrasena) {
        // Buscar la cuenta por correo
        Cuenta cuenta = cuentaRepositorio.findByUsuario(correo)
            .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
    
        // Verificar si la cuenta está ACTIVA
        if (cuenta.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("La cuenta no está activa");
        }
    
        // Validar contraseña encriptada
        if (!passwordEncoder.matches(contrasena, cuenta.getContrasena())) {

            throw new IllegalArgumentException("Credenciales inválidas");
        }
    
        // Obtener y retornar el arrendador asociado
        return arrendadorRepositorio.findByCuenta(cuenta)
            .orElseThrow(() -> new IllegalArgumentException("Arrendador no encontrado"));
    }

    public ArrendadorDto obtenerArrendadorPorCorreo(String correo) {
        Arrendador arrendador = arrendadorRepositorio.findByCorreo(correo)
            .orElseThrow(() -> new IllegalArgumentException("Arrendador no encontrado"));
        
        return modelMapper.map(arrendador, ArrendadorDto.class);
    }

    

}
