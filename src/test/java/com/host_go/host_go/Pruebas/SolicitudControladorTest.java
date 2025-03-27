package com.host_go.host_go.Pruebas;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.host_go.host_go.modelos.Arrendatario;
import com.host_go.host_go.modelos.Cuenta;
import com.host_go.host_go.modelos.Status;
import com.host_go.host_go.Repositorios.ArrendatarioRepositorio;
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
public class SolicitudControladorTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private CuentaRepositorio cuentaRepositorio;
    
    @Autowired
    private ArrendatarioRepositorio arrendatarioRepositorio;
    
    private Cuenta cuenta;
    private Arrendatario arrendatario;
    
    @BeforeEach
    public void setup() {
        // Simulamos la existencia de una cuenta y arrendatario de prueba para la autenticación.
        String username = "test_arrendatario@javeriana.edu.co";
        cuenta = cuentaRepositorio.findByUsuario(username).orElse(null);
        if (cuenta == null) {
            cuenta = new Cuenta();
            cuenta.setUsuario(username);
            cuenta.setContrasena("dummy"); // valor dummy
            cuenta.setTipo("ARRENDATARIO");
            cuenta.setStatus(Status.ACTIVE);
            cuenta = cuentaRepositorio.save(cuenta);
        }
        
        arrendatario = arrendatarioRepositorio.findByCuenta(cuenta).orElse(null);
        if (arrendatario == null) {
            arrendatario = new Arrendatario();
            arrendatario.setCedula(111222333);
            arrendatario.setNombre("Test");
            arrendatario.setApellido("Arrendatario");
            arrendatario.setCorreo(username);
            arrendatario.setTelefono(300987654);
            arrendatario.setStatus(Status.ACTIVE);
            arrendatario.setCuenta(cuenta);
            arrendatario = arrendatarioRepositorio.save(arrendatario);
        }
    }
    
    /**
     * Esta prueba simula que un arrendatario autenticado consulta todas las solicitudes
     * que ha hecho (endpoint: GET /Solicitud/misSolicitudes).
     */
    @Test
    @WithMockUser(username = "test_arrendatario@javeriana.edu.co", roles = {"ARRENDATARIO"})
    public void testObtenerMisSolicitudes_Success() throws Exception {
        // Se asume que existen solicitudes en la base de datos para este arrendatario.
        // Si fuera necesario, puedes crear algunas solicitudes de prueba en el setup.
        mockMvc.perform(get("/Solicitud/misSolicitudes")
                .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}
