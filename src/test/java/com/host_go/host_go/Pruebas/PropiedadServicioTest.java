package com.host_go.host_go.Pruebas;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.host_go.host_go.Dtos.PropiedadDto;
import com.host_go.host_go.Dtos.ArrendadorDto;
import com.host_go.host_go.Repositorios.ArrendadorRepositorio;
import com.host_go.host_go.Servicios.PropiedadServicio;
import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PropiedadServicioTest {

    @Autowired
    private PropiedadServicio propiedadServicio;
    
    @Autowired
    private ArrendadorRepositorio arrendadorRepositorio;
    
    private Arrendador arrendador;

    @BeforeEach
    public void setup() {
        // Verificar si ya existe un arrendador de prueba; si no, crearlo.
        // Aquí se asume que no tienes datos persistentes de pruebas previos.
        long testCedula = 111222333L;
        arrendador = arrendadorRepositorio.findById(testCedula).orElse(null);
        if(arrendador == null) {
            arrendador = new Arrendador();
            arrendador.setCedula((int)testCedula);
            arrendador.setNombre("Propiedad");
            arrendador.setApellido("Tester");
            arrendador.setCorreo("propiedad_test@javeriana.edu.co");
            arrendador.setTelefono(300000000);
            arrendador.setStatus(Status.ACTIVE);
            // Para esta prueba no es esencial tener una cuenta completa
            arrendador = arrendadorRepositorio.save(arrendador);
        }
    }

    @Test
    public void testSave_ValidPropiedad_ReturnsPropiedadDto() {
        // Arrange: preparar datos válidos
        PropiedadDto dto = new PropiedadDto();
        dto.setNombre("Casa en la montaña");
        dto.setDepartamento("Antioquia");
        dto.setMunicipio("Guatapé");
        dto.setTipoIngreso("Secundaria");
        dto.setDescripcion("Hermosa casa con vista.");
        dto.setCapacidad(6);
        dto.setHabitaciones(3);
        dto.setBanos(2);
        dto.setPermiteMascotas(true);
        dto.setTienePiscina(true);
        dto.setTieneAsador(true);
        dto.setValorNoche(250000);
        
        // Asignamos un arrendador usando solo su id (ya creado en setup)
        ArrendadorDto arrDto = new ArrendadorDto();
        arrDto.setArrendadorId(arrendador.getArrendadorId());
        dto.setArrendador(arrDto);
        
        // Act
        PropiedadDto result = propiedadServicio.save(dto);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getPropiedadId() > 0);
        assertEquals("Casa en la montaña", result.getNombre());
        assertEquals("Antioquia", result.getDepartamento());
        assertEquals("Guatapé", result.getMunicipio());
        // Puedes agregar más aserciones según tus necesidades
    }
    
    @Test
    public void testSave_InvalidPropiedad_MissingName_ThrowsException() {
        // Arrange: preparar datos inválidos (nombre vacío)
        PropiedadDto dto = new PropiedadDto();
        dto.setNombre(""); // nombre vacío
        dto.setDepartamento("Antioquia");
        dto.setMunicipio("Guatapé");
        dto.setTipoIngreso("Secundaria");
        dto.setDescripcion("Hermosa casa con vista.");
        dto.setCapacidad(6);
        dto.setHabitaciones(3);
        dto.setBanos(2);
        dto.setPermiteMascotas(true);
        dto.setTienePiscina(true);
        dto.setTieneAsador(true);
        dto.setValorNoche(250000);
        
        ArrendadorDto arrDto = new ArrendadorDto();
        arrDto.setArrendadorId(arrendador.getArrendadorId());
        dto.setArrendador(arrDto);
        
        // Act & Assert: se espera que lance una excepción con mensaje "El nombre es obligatorio."
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            propiedadServicio.save(dto);
        });
        assertEquals("El nombre es obligatorio.", exception.getMessage());
    }

    @Test
    public void testUpdate_ValidPropiedad_UpdatesSuccessfully() {
        // Primero: crear una propiedad válida
        PropiedadDto dto = new PropiedadDto();
        dto.setNombre("Casa Original");
        dto.setDepartamento("Antioquia");
        dto.setMunicipio("Guatapé");
        dto.setTipoIngreso("Secundaria");
        dto.setDescripcion("Descripción original");
        dto.setCapacidad(6);
        dto.setHabitaciones(3);
        dto.setBanos(2);
        dto.setPermiteMascotas(true);
        dto.setTienePiscina(true);
        dto.setTieneAsador(true);
        dto.setValorNoche(250000);
        
        // Asignar el arrendador usando su ID (ya creado en setup)
        ArrendadorDto arrDto = new ArrendadorDto();
        arrDto.setArrendadorId(arrendador.getArrendadorId());
        dto.setArrendador(arrDto);
        
        PropiedadDto propiedadCreada = propiedadServicio.save(dto);
        assertNotNull(propiedadCreada);
        assertEquals("Casa Original", propiedadCreada.getNombre());
        
        // Ahora, modificar algunos campos
        propiedadCreada.setNombre("Casa Actualizada");
        propiedadCreada.setDescripcion("Descripción actualizada");
        
        // Act: Llamada al método update
        PropiedadDto propiedadActualizada = propiedadServicio.update(propiedadCreada);
        
        // Assert: verificar que los cambios se hayan guardado
        assertNotNull(propiedadActualizada);
        assertEquals("Casa Actualizada", propiedadActualizada.getNombre());
        assertEquals("Descripción actualizada", propiedadActualizada.getDescripcion());
    }

    @Test
    public void testUpdate_InvalidPropiedad_ThrowsException() {
        // Crear un DTO con un ID inexistente (suponiendo que 999999 no existe)
        PropiedadDto dto = new PropiedadDto();
        dto.setPropiedadId(999999L);
        dto.setNombre("Propiedad Inexistente");
        dto.setDepartamento("Antioquia");
        dto.setMunicipio("Guatapé");
        dto.setTipoIngreso("Secundaria");
        dto.setDescripcion("Descripción");
        dto.setCapacidad(6);
        dto.setHabitaciones(3);
        dto.setBanos(2);
        dto.setPermiteMascotas(true);
        dto.setTienePiscina(true);
        dto.setTieneAsador(true);
        dto.setValorNoche(250000);
        
        ArrendadorDto arrDto = new ArrendadorDto();
        // Este ID puede ser cualquiera, ya que no se validará al no encontrar la propiedad
        arrDto.setArrendadorId(arrendador.getArrendadorId());
        dto.setArrendador(arrDto);
        
        // Act & Assert: se espera que se lance excepción "Propiedad no encontrada"
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            propiedadServicio.update(dto);
        });
        assertEquals("Propiedad no encontrada", exception.getMessage());
    }

    @Test
    public void testDesactivarPropiedad_Success() {
        // Arrange: Crear una propiedad válida
        PropiedadDto dto = new PropiedadDto();
        dto.setNombre("Casa de Prueba");
        dto.setDepartamento("Antioquia");
        dto.setMunicipio("Guatapé");
        dto.setTipoIngreso("Secundaria");
        dto.setDescripcion("Hermosa casa de prueba");
        dto.setCapacidad(6);
        dto.setHabitaciones(3);
        dto.setBanos(2);
        dto.setPermiteMascotas(true);
        dto.setTienePiscina(true);
        dto.setTieneAsador(true);
        dto.setValorNoche(250000);
        
        // Asignar el arrendador (usando solo su ID ya creado en setup)
        ArrendadorDto arrDto = new ArrendadorDto();
        arrDto.setArrendadorId(arrendador.getArrendadorId());
        dto.setArrendador(arrDto);
        
        // Se crea la propiedad (estado ACTIVE)
        PropiedadDto propiedadCreada = propiedadServicio.save(dto);
        assertNotNull(propiedadCreada);
        assertTrue(propiedadCreada.getPropiedadId() > 0);
        // Se espera que al crear la propiedad, su estado sea ACTIVE
        assertEquals(Status.ACTIVE, propiedadCreada.getStatus());
        
        // Act: Desactivar la propiedad
        PropiedadDto propiedadDesactivada = propiedadServicio.desactivarPropiedad(propiedadCreada.getPropiedadId());
        
        // Assert: Verificar que la propiedad se haya desactivado (estado INACTIVE)
        assertNotNull(propiedadDesactivada);
        assertEquals(Status.INACTIVE, propiedadDesactivada.getStatus());
    }
    
    @Test
    public void testDesactivarPropiedad_PropertyNotFound_ThrowsException() {
        // Act & Assert: Se espera excepción si se intenta desactivar una propiedad inexistente.
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            propiedadServicio.desactivarPropiedad(999999L);
        });
        assertEquals("Propiedad no encontrada", exception.getMessage());
    }

    @Test
    public void testBuscarPropiedades_Success() {
        // Dado que en el setup se creó la propiedad "Casa de Arriendo" en "Guatapé" con capacidad 6,
        // buscamos propiedades cuyo nombre contenga "casa", municipio "Guatapé" y capacidad >= 6.
        List<PropiedadDto> resultados = propiedadServicio.buscarPropiedades("casa", "Guatapé", 6);
        
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "Se debería encontrar al menos una propiedad que cumpla los criterios");
        // Opcional: verificar que el primer resultado contenga "Casa de Arriendo" en su nombre.
        assertTrue(resultados.get(0).getNombre().toLowerCase().contains("casa"));
    }

    @Test
    public void testBuscarPropiedades_NoResults() {
        // Buscamos con criterios que no coincidan con ninguna propiedad (por ejemplo, nombre "xyz")
        List<PropiedadDto> resultados = propiedadServicio.buscarPropiedades("xyz", "NoExistente", 10);
        
        assertNotNull(resultados);
        assertTrue(resultados.isEmpty(), "No se debería encontrar ninguna propiedad con esos criterios");
    }


}
