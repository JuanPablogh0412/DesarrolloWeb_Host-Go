package com.host_go.host_go.Pruebas;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.host_go.host_go.Repositorios.ActivationTokenRepositorio;
import com.host_go.host_go.Repositorios.ArrendadorRepositorio;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import com.host_go.host_go.modelos.ActivationToken;
import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Cuenta;
import com.host_go.host_go.modelos.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivacionControladorTest {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ActivationTokenRepositorio tokenRepositorio;
    
    @Autowired
    private CuentaRepositorio cuentaRepositorio;
    
    @Autowired
    private ArrendadorRepositorio arrendadorRepositorio;
    
    @Test
    public void testActivateAccount_Success() {
        // 1. Crear una cuenta de tipo ARRENDADOR con estado INACTIVE
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario("test@javeriana.edu.co");
        cuenta.setContrasena("dummy");  // La contraseña no es relevante para la activación
        cuenta.setTipo("ARRENDADOR");
        cuenta.setStatus(Status.INACTIVE);
        cuenta = cuentaRepositorio.save(cuenta);
        
        // 2. Crear un arrendador asociado a la cuenta, también INACTIVE
        Arrendador arrendador = new Arrendador();
        arrendador.setCedula(123456);
        arrendador.setNombre("Test");
        arrendador.setApellido("User");
        arrendador.setCorreo(cuenta.getUsuario());
        arrendador.setTelefono(300123456);
        arrendador.setCuenta(cuenta);
        arrendador.setStatus(Status.INACTIVE);
        arrendador = arrendadorRepositorio.save(arrendador);
        
        // 3. Crear y guardar un ActivationToken con token fijo "test-token" y expiración futura
        ActivationToken activationToken = new ActivationToken();
        activationToken.setToken("test-token");
        activationToken.setExpirationDate(LocalDateTime.now().plusHours(1));
        activationToken.setCuenta(cuenta);
        activationToken = tokenRepositorio.save(activationToken);
        
        // 4. Realizar llamada GET al endpoint de activación
        String url = "/auth/activate?token=test-token";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // 5. Verificar la respuesta HTTP
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cuenta activada exitosamente", response.getBody());
        
        // 6. Verificar que la cuenta ahora está en estado ACTIVE
        Cuenta updatedCuenta = cuentaRepositorio.findById(cuenta.getCuentaId()).orElse(null);
        assertNotNull(updatedCuenta);
        assertEquals(Status.ACTIVE, updatedCuenta.getStatus());
        
        // 7. Verificar que el arrendador ahora está en estado ACTIVE
        Arrendador updatedArrendador = arrendadorRepositorio.findById(arrendador.getArrendadorId()).orElse(null);
        assertNotNull(updatedArrendador);
        assertEquals(Status.ACTIVE, updatedArrendador.getStatus());
        
        // 8. Verificar que el token ha sido eliminado del repositorio
        Optional<ActivationToken> tokenOptional = tokenRepositorio.findByToken("test-token");
        assertFalse(tokenOptional.isPresent());
    }
}