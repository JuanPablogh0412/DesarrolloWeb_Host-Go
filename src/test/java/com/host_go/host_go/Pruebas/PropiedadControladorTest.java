package com.host_go.host_go.Pruebas;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Cuenta;
import com.host_go.host_go.modelos.Status;
import com.host_go.host_go.Repositorios.ArrendadorRepositorio;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PropiedadControladorTest {

    @Autowired
    private MockMvc mockMvc;

    
    @Autowired
    private CuentaRepositorio cuentaRepositorio;
    
    @Autowired
    private ArrendadorRepositorio arrendadorRepositorio;
    
    @BeforeEach
    public void setup() {
        // Crear una cuenta y arrendador de prueba para el usuario autenticado
        // Nota: Si ya existe, en un entorno real usarías un mecanismo para limpiar la BD de pruebas.
        String testEmail = "arrendador_test@javeriana.edu.co";
        Cuenta cuenta = cuentaRepositorio.findByUsuario(testEmail).orElse(null);
        if(cuenta == null) {
            cuenta = new Cuenta();
            cuenta.setUsuario(testEmail);
            cuenta.setContrasena("dummy"); // no importa el valor aquí
            cuenta.setTipo("ARRENDADOR");
            cuenta.setStatus(Status.ACTIVE);
            cuenta = cuentaRepositorio.save(cuenta);
        }
        
        Arrendador arrendador = arrendadorRepositorio.findByCuenta(cuenta).orElse(null);
        if(arrendador == null) {
            arrendador = new Arrendador();
            arrendador.setCedula(123456789);
            arrendador.setNombre("Test");
            arrendador.setApellido("Arrendador");
            arrendador.setCorreo(testEmail);
            arrendador.setTelefono(300111222);
            arrendador.setCuenta(cuenta);
            arrendador.setStatus(Status.ACTIVE);
            arrendadorRepositorio.save(arrendador);
        }
    }
    
    /**
     * Esta prueba simula que un arrendador autenticado consulta sus propiedades.
     * Se espera que el endpoint /Propiedad/misPropiedades retorne un arreglo JSON.
     */
    @Test
    @WithMockUser(username = "arrendador_test@javeriana.edu.co", roles = {"ARRENDADOR"})
    public void testGetMisPropiedades() throws Exception {
        // Realizamos la llamada GET al endpoint /Propiedad/misPropiedades
        mockMvc.perform(get("/Propiedad/misPropiedades")
                .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}