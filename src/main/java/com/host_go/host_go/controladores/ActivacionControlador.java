package com.host_go.host_go.controladores;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Config.JwtUtils;
import com.host_go.host_go.Dtos.ArrendadorDto;
import com.host_go.host_go.Dtos.ArrendatarioDto;
import com.host_go.host_go.Dtos.LoginRequestDto;
import com.host_go.host_go.Repositorios.ActivationTokenRepositorio;
import com.host_go.host_go.Repositorios.ArrendadorRepositorio;
import com.host_go.host_go.Repositorios.ArrendatarioRepositorio;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import com.host_go.host_go.Servicios.ArrendadorServicio;
import com.host_go.host_go.Servicios.ArrendatarioServicio;
import com.host_go.host_go.modelos.ActivationToken;
import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Arrendatario;
import com.host_go.host_go.modelos.Cuenta;
import com.host_go.host_go.modelos.Status;

@RestController
@RequestMapping("/auth")
public class ActivacionControlador {
      @Autowired
    private ActivationTokenRepositorio tokenRepositorio;
    
    @Autowired
    private CuentaRepositorio cuentaRepositorio;
    
    @Autowired
    private ArrendadorRepositorio arrendadorRepositorio;

    @Autowired
    private ArrendatarioRepositorio arrendatarioRepositorio;
    
    @Autowired
    private ArrendadorServicio arrendadorServicio;

    @Autowired
    private ArrendatarioServicio arrendatarioServicio;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam String token) {
        ActivationToken activationToken = tokenRepositorio.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (LocalDateTime.now().isAfter(activationToken.getExpirationDate())) {
            throw new IllegalArgumentException("Token expirado");
        }

        Cuenta cuenta = activationToken.getCuenta();
        cuenta.setStatus(Status.ACTIVE);
        cuentaRepositorio.save(cuenta);


        if(cuenta.getTipo().equals("ARRENDADOR")){
            Arrendador arrendador = arrendadorRepositorio.findByCuenta(cuenta)
            .orElseThrow(() -> new IllegalArgumentException("Arrendador no encontrado"));
            arrendador.setStatus(Status.ACTIVE);
            arrendadorRepositorio.save(arrendador);
        }
        else if(cuenta.getTipo().equals("ARRENDATARIO")){
            Arrendatario arrendatario = arrendatarioRepositorio.findByCuenta(cuenta)
            .orElseThrow(() -> new IllegalArgumentException("Arrendatario no encontrado"));
            arrendatario.setStatus(Status.ACTIVE);
            arrendatarioRepositorio.save(arrendatario);
        }
        

        // Eliminar token usado
        tokenRepositorio.delete(activationToken);

        return ResponseEntity.ok("Cuenta activada exitosamente");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            // Autenticar usando Spring Security
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getCorreo(),
                    loginRequest.getContrasena()
                )
            );

            // Generar token JWT
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails);

            Cuenta cuenta = cuentaRepositorio.findByUsuario(loginRequest.getCorreo())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));  

            if(cuenta.getTipo().equals("ARRENDADOR")){
                ArrendadorDto arrendador = arrendadorServicio.obtenerArrendadorPorCorreo(loginRequest.getCorreo());
                // Obtener datos del arrendador para la respuesta
                return ResponseEntity.ok(Map.of(
                    "token", token,
                    "arrendador", arrendador
                ));
            }

            if(cuenta.getTipo().equals("ARRENDATARIO")){
                ArrendatarioDto arrendatario = arrendatarioServicio.obtenerarrendatarioPorCorreo(loginRequest.getCorreo());
                // Obtener datos del arrendador para la respuesta
                return ResponseEntity.ok(Map.of(
                    "token", token,
                    "arrendatario", arrendatario
                ));
            }
            


            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Credenciales inválidas"));
        }
                return ResponseEntity.badRequest().body(Map.of("error", "No se encontro ninguna cuenta"));
    }
}
