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

import com.host_go.host_go.Dtos.ArrendatarioCreateDto;
import com.host_go.host_go.Dtos.ArrendatarioDto;
import com.host_go.host_go.Repositorios.ActivationTokenRepositorio;
import com.host_go.host_go.Repositorios.ArrendatarioRepositorio;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import com.host_go.host_go.modelos.ActivationToken;
import com.host_go.host_go.modelos.Arrendatario;
import com.host_go.host_go.modelos.Cuenta;
import com.host_go.host_go.modelos.Status;

@Service
public class ArrendatarioServicio {

    @Autowired
    ArrendatarioRepositorio ArrendatarioRepositorio;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private CuentaRepositorio cuentaRepositorio;
    @Autowired
    private PasswordEncoder passwordEncoder; 
    @Autowired
    private ActivationTokenRepositorio activationTokenRepositorio;
    @Autowired
    private EmailServicio emailService;


    public ArrendatarioDto get(Long id){
        Optional<Arrendatario> ArrendatarioOptional = ArrendatarioRepositorio.findById(id);
        ArrendatarioDto ArrendatarioDto = null;
        if( ArrendatarioOptional != null){
            ArrendatarioDto = modelMapper.map(ArrendatarioOptional.get(), ArrendatarioDto.class);
        }
        return ArrendatarioDto;
    }

    public List<ArrendatarioDto> get( ){
        List<Arrendatario>Arrendatarios = (List<Arrendatario>) ArrendatarioRepositorio.findAll();
        List<ArrendatarioDto> ArrendatarioDtos = Arrendatarios.stream().map(Arrendatario -> modelMapper.map(Arrendatario, ArrendatarioDto.class)).collect(Collectors.toList());
        return ArrendatarioDtos;
    }

    public ArrendatarioDto save(ArrendatarioCreateDto arrendatarioCreateDto) {
        // Validar correo único
        if (ArrendatarioRepositorio.existsByCorreo(arrendatarioCreateDto.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // Validar formato de correo corporativo
        if (!arrendatarioCreateDto.getCorreo().endsWith("@javeriana.edu.co")) {
            throw new IllegalArgumentException("Correo no cumple con el formato corporativo");
        }

        // Validar contraseña
        if (arrendatarioCreateDto.getContrasena().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }

        // Crear la cuenta asociada
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(arrendatarioCreateDto.getCorreo());
        cuenta.setContrasena(passwordEncoder.encode(arrendatarioCreateDto.getContrasena()));
        cuenta.setTipo("ARRENDATARIO");
        cuenta.setStatus(Status.INACTIVE);
        cuenta = cuentaRepositorio.save(cuenta);
        // Generar token de activación
        ActivationToken activationToken = createActivationToken(cuenta);
        
        // Enviar correo
        String activationLink = "http://localhost:8080/auth/activate?token=" + activationToken.getToken();
        emailService.sendActivationEmail(cuenta.getUsuario(), activationLink);

        // Crear el arrendatario
        Arrendatario arrendatario = modelMapper.map(arrendatarioCreateDto, Arrendatario.class);
        arrendatario.setCuenta(cuenta);
        arrendatario.setStatus(Status.ACTIVE);
        arrendatario = ArrendatarioRepositorio.save(arrendatario);

        // Convertir a DTO de respuesta (sin contraseña)
        ArrendatarioDto arrendatarioDto = modelMapper.map(arrendatario, ArrendatarioDto.class);
        return arrendatarioDto;
    }

    public ArrendatarioDto update (ArrendatarioDto ArrendatarioDto) throws ValidationException{
        ArrendatarioDto = get(ArrendatarioDto.getArrendatarioId());
        if(ArrendatarioDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        Arrendatario Arrendatario = modelMapper.map(ArrendatarioDto, Arrendatario.class);
        Arrendatario.setStatus(Status.ACTIVE);
        Arrendatario = ArrendatarioRepositorio.save(Arrendatario);
        ArrendatarioDto = modelMapper.map(Arrendatario, ArrendatarioDto.class);
        return ArrendatarioDto;
    }

    public void delete (Long id){
        ArrendatarioRepositorio.deleteById(id);
    }

    private ActivationToken createActivationToken(Cuenta cuenta) {
        ActivationToken token = new ActivationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpirationDate(LocalDateTime.now().plusHours(24));
        token.setCuenta(cuenta);
        return activationTokenRepositorio.save(token);
    }

    public Arrendatario validarCredenciales(String correo, String contrasena) {
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
        return ArrendatarioRepositorio.findByCuenta(cuenta)
            .orElseThrow(() -> new IllegalArgumentException("Arrendador no encontrado"));
    }

    public ArrendatarioDto obtenerarrendatarioPorCorreo(String correo) {
        Arrendatario arrendatario = ArrendatarioRepositorio.findByCorreo(correo)
            .orElseThrow(() -> new IllegalArgumentException("Arrendatario no encontrado"));
        
        return modelMapper.map(arrendatario, ArrendatarioDto.class);
    }

    

}
