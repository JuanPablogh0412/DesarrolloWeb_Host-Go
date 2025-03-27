package com.host_go.host_go.Pruebas;

import static org.junit.jupiter.api.Assertions.*;

import com.host_go.host_go.Dtos.CalificacionDto;
import com.host_go.host_go.Dtos.CuentaDto;
import com.host_go.host_go.modelos.Cuenta;
import com.host_go.host_go.modelos.Status;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import com.host_go.host_go.Servicios.CalificacionServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CalificacionServicioTest {

    @Autowired
    private CalificacionServicio calificacionServicio;
    
    @Autowired
    private CuentaRepositorio cuentaRepositorio;
    
    private Cuenta cuenta;
    
    @BeforeEach
    public void setup() {
        // Intentar recuperar una cuenta de prueba; si no existe, se crea
        long cuentaIdDePrueba = 1L;
        cuenta = cuentaRepositorio.findById(cuentaIdDePrueba).orElse(null);
        if (cuenta == null) {
            cuenta = new Cuenta();
            cuenta.setUsuario("calificacion_test@javeriana.edu.co");
            cuenta.setContrasena("dummy"); // valor dummy
            cuenta.setTipo("ARRENDATARIO");
            cuenta.setStatus(Status.ACTIVE);
            cuenta = cuentaRepositorio.save(cuenta);
        }
    }
    
    @Test
    public void testSave_ValidCalificacion_ReturnsCalificacionDto() {
        // Arrange: Se crea un DTO válido para calificar con estrellas válidas (entre 1 y 5)
        CalificacionDto dto = new CalificacionDto();
        dto.setEstrellas(4);
        dto.setComentario("Buen arrendatario");
        
        // Se asigna la cuenta al DTO (solo se requiere el ID)
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setCuentaId(cuenta.getCuentaId());
        dto.setCuenta(cuentaDto);
        
        // Act: Se guarda la calificación
        CalificacionDto saved = calificacionServicio.save(dto);
        
        // Assert: Se verifica que se haya creado correctamente
        assertNotNull(saved, "El DTO de calificación guardada no debe ser nulo");
        assertTrue(saved.getCalificacionId() > 0, "El ID de la calificación debe ser mayor que 0");
        assertEquals(4, saved.getEstrellas(), "Las estrellas deben ser 4");
        assertEquals("Buen arrendatario", saved.getComentario(), "El comentario debe coincidir");
        // Se espera que el status en la entidad sea ACTIVE, aunque no se exponga en el DTO
    }
    
    @Test
    public void testSave_InvalidEstrellas_ThrowsException() {
        // Arrange: Se crea un DTO con estrellas inválidas (por ejemplo, 6)
        CalificacionDto dto = new CalificacionDto();
        dto.setEstrellas(6); // inválido, debe ser entre 1 y 5
        dto.setComentario("Error test");
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setCuentaId(cuenta.getCuentaId());
        dto.setCuenta(cuentaDto);
        
        // Act & Assert: Se espera que se lance una excepción
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            calificacionServicio.save(dto);
        });
        assertEquals("La calificación debe ser entre 1 y 5 estrellas", ex.getMessage());
    }
}
