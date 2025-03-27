package com.host_go.host_go.Pruebas;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import com.host_go.host_go.Dtos.LoginRequestDto;
import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Cuenta;
import com.host_go.host_go.modelos.Status;
import com.host_go.host_go.Repositorios.ArrendadorRepositorio;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginArrendadorTest {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private CuentaRepositorio cuentaRepositorio;
    
    @Autowired
    private ArrendadorRepositorio arrendadorRepositorio;
    
    @Autowired
    private PasswordEncoder passwordEncoder; // Para codificar la contraseña

    @SuppressWarnings("rawtypes")
    @Test
    public void testLoginArrendador_Success() {
        // Crear una cuenta activa para ARRENDADOR
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario("login_test@javeriana.edu.co");
        cuenta.setContrasena(passwordEncoder.encode("password123"));
        cuenta.setTipo("ARRENDADOR");
        cuenta.setStatus(Status.ACTIVE);
        cuenta = cuentaRepositorio.save(cuenta);
        
        // Crear un arrendador asociado a la cuenta
        Arrendador arrendador = new Arrendador();
        arrendador.setCedula(987654321);
        arrendador.setNombre("Login");
        arrendador.setApellido("Test");
        arrendador.setCorreo(cuenta.getUsuario());
        arrendador.setTelefono(300987654);
        arrendador.setCuenta(cuenta);
        arrendador.setStatus(Status.ACTIVE);
        arrendador = arrendadorRepositorio.save(arrendador);
        
        // Preparar el objeto de solicitud de login
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setCorreo("login_test@javeriana.edu.co");
        loginRequest.setContrasena("password123");

        // Llamar al endpoint /auth/login vía POST
        ResponseEntity<Map> response = restTemplate.postForEntity("/auth/login", loginRequest, Map.class);
        
        // Verificar que la respuesta es 200 OK y contiene un token y los datos del arrendador
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map body = response.getBody();
        assertNotNull(body.get("token"), "El token no debería ser nulo");
        assertNotNull(body.get("arrendador"), "El objeto arrendador no debería ser nulo");
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testLoginArrendador_InvalidPassword() {
        // Crear una cuenta activa para ARRENDADOR
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario("login_fail@javeriana.edu.co");
        cuenta.setContrasena(passwordEncoder.encode("password123"));
        cuenta.setTipo("ARRENDADOR");
        cuenta.setStatus(Status.ACTIVE);
        cuenta = cuentaRepositorio.save(cuenta);

        // Crear un arrendador asociado a la cuenta
        Arrendador arrendador = new Arrendador();
        arrendador.setCedula(111222333);
        arrendador.setNombre("Fail");
        arrendador.setApellido("Test");
        arrendador.setCorreo(cuenta.getUsuario());
        arrendador.setTelefono(300111222);
        arrendador.setCuenta(cuenta);
        arrendador.setStatus(Status.ACTIVE);
        arrendador = arrendadorRepositorio.save(arrendador);

        // Preparar la solicitud de login con contraseña incorrecta
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setCorreo("login_fail@javeriana.edu.co");
        loginRequest.setContrasena("wrongpassword");

        ResponseEntity<Map> response = restTemplate.postForEntity("/auth/login", loginRequest, Map.class);
        
        // Esperamos una respuesta 400 Bad Request con mensaje de error
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map body = response.getBody();
        assertNotNull(body.get("error"), "Se esperaba un mensaje de error");
    }
}