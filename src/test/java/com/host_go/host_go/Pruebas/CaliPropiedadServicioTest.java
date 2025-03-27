package com.host_go.host_go.Pruebas;

import static org.junit.jupiter.api.Assertions.*;

import com.host_go.host_go.Dtos.CaliPropiedadDto;
import com.host_go.host_go.Dtos.PropiedadDto;
import com.host_go.host_go.Repositorios.PropiedadRepositorio;
import com.host_go.host_go.Servicios.CaliPropiedadServicio;
import com.host_go.host_go.modelos.Propiedad;
import com.host_go.host_go.modelos.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CaliPropiedadServicioTest {

    @Autowired
    private CaliPropiedadServicio caliPropiedadServicio;
    
    @Autowired
    private PropiedadRepositorio propiedadRepositorio;
    
    private Propiedad propiedad;
    
    @BeforeEach
    public void setup() {
        // Se busca o se crea una propiedad de prueba para calificar.
        // Se asume que se desea calificar una propiedad con ID 1.
        propiedad = propiedadRepositorio.findById(1L).orElse(null);
        if(propiedad == null) {
            propiedad = new Propiedad();
            propiedad.setNombre("Propiedad de Prueba");
            propiedad.setDepartamento("Antioquia");
            propiedad.setMunicipio("Guatapé");
            propiedad.setTipoIngreso("Secundaria");
            propiedad.setDescripcion("Propiedad para pruebas");
            propiedad.setCapacidad(6);
            propiedad.setHabitaciones(3);
            propiedad.setBanos(2);
            propiedad.setPermiteMascotas(true);
            propiedad.setTienePiscina(true);
            propiedad.setTieneAsador(true);
            propiedad.setValorNoche(250000);
            propiedad.setStatus(Status.ACTIVE);
            // Para simplificar, no se asocia arrendador en esta prueba
            propiedad = propiedadRepositorio.save(propiedad);
        }
    }
    
    @Test
    public void testSave_ValidCalificacion_Success() {
        // Arrange: Crear un DTO válido para calificar la propiedad
        CaliPropiedadDto dto = new CaliPropiedadDto();
        dto.setEstrellas(4);
        dto.setComentario("Buena propiedad, muy limpia.");
        
        // Se asigna el DTO de propiedad con el ID de la propiedad de prueba
        PropiedadDto propDto = new PropiedadDto();
        propDto.setPropiedadId(propiedad.getPropiedadId());
        dto.setPropiedad(propDto);
        
        // Act: Guardar la calificación
        CaliPropiedadDto saved = caliPropiedadServicio.save(dto);
        
        // Assert: Verificar que se haya creado correctamente
        assertNotNull(saved, "El DTO de calificación guardada no debe ser nulo");
        assertTrue(saved.getCaliPropiedadId() > 0, "El ID de la calificación debe ser mayor que 0");
        assertEquals(4, saved.getEstrellas(), "Las estrellas deben ser 4");
        assertEquals("Buena propiedad, muy limpia.", saved.getComentario(), "El comentario debe coincidir");
    }
    
    @Test
    public void testSave_InvalidEstrellas_ThrowsException() {
        // Arrange: Crear un DTO con estrellas inválidas (por ejemplo, 6)
        CaliPropiedadDto dto = new CaliPropiedadDto();
        dto.setEstrellas(6); // Valor fuera del rango 1 a 5
        dto.setComentario("Calificación incorrecta");
        
        PropiedadDto propDto = new PropiedadDto();
        propDto.setPropiedadId(propiedad.getPropiedadId());
        dto.setPropiedad(propDto);
        
        // Act & Assert: Se espera que se lance una excepción con el mensaje adecuado
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            caliPropiedadServicio.save(dto);
        });
        assertEquals("La calificación debe ser entre 1 y 5 estrellas", exception.getMessage());
    }
}
