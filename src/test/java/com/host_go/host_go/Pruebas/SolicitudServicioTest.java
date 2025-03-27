package com.host_go.host_go.Pruebas;

import static org.junit.jupiter.api.Assertions.*;

import com.host_go.host_go.Dtos.SolicitudDto;
import com.host_go.host_go.modelos.Arrendatario;
import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Propiedad;
import com.host_go.host_go.modelos.Solicitud;
import com.host_go.host_go.modelos.Status;
import com.host_go.host_go.Repositorios.ArrendatarioRepositorio;
import com.host_go.host_go.Repositorios.ArrendadorRepositorio;
import com.host_go.host_go.Repositorios.PropiedadRepositorio;
import com.host_go.host_go.Repositorios.SolicitudRepositorio;
import com.host_go.host_go.Servicios.SolicitudServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class SolicitudServicioTest {

    @Autowired
    private SolicitudServicio solicitudServicio;
    
    @Autowired
    private PropiedadRepositorio propiedadRepositorio;
    
    @Autowired
    private SolicitudRepositorio solicitudRepositorio;
    
    @Autowired
    private ArrendadorRepositorio arrendadorRepositorio;
    
    @Autowired
    private ArrendatarioRepositorio arrendatarioRepositorio;
    
    private Arrendador arrendador;
    private Propiedad propiedad;
    private Arrendatario arrendatario;
    
    @BeforeEach
    public void setup() {
        // Crear arrendador de prueba (si no existe)
        long cedulaArr = 123456;
        arrendador = arrendadorRepositorio.findById(cedulaArr).orElse(null);
        if (arrendador == null) {
            arrendador = new Arrendador();
            arrendador.setCedula((int) cedulaArr);
            arrendador.setNombre("Test");
            arrendador.setApellido("Arrendador");
            arrendador.setCorreo("test_arrendador@javeriana.edu.co");
            arrendador.setTelefono(300123456);
            arrendador.setStatus(Status.ACTIVE);
            arrendador = arrendadorRepositorio.save(arrendador);
        }
        
        // Crear propiedad de prueba asociada al arrendador
        propiedad = new Propiedad();
        propiedad.setNombre("Casa de Arriendo");
        propiedad.setDepartamento("Antioquia");
        propiedad.setMunicipio("Guatapé");
        propiedad.setTipoIngreso("Secundaria");
        propiedad.setDescripcion("Casa para arriendo de prueba");
        propiedad.setCapacidad(6);
        propiedad.setHabitaciones(3);
        propiedad.setBanos(2);
        propiedad.setPermiteMascotas(true);
        propiedad.setTienePiscina(true);
        propiedad.setTieneAsador(true);
        propiedad.setValorNoche(250000);
        propiedad.setStatus(Status.ACTIVE);
        propiedad.setArrendador(arrendador);
        propiedad = propiedadRepositorio.save(propiedad);
        
        // Crear un arrendatario de prueba
        long cedulaArrendatario = 111222333;
        arrendatario = arrendatarioRepositorio.findById(cedulaArrendatario).orElse(null);
        if(arrendatario == null) {
            arrendatario = new Arrendatario();
            arrendatario.setCedula((int) cedulaArrendatario);
            arrendatario.setNombre("Test");
            arrendatario.setApellido("Arrendatario");
            arrendatario.setCorreo("test_arrendatario@javeriana.edu.co");
            arrendatario.setTelefono(300987654);
            arrendatario.setStatus(Status.ACTIVE);
            arrendatario = arrendatarioRepositorio.save(arrendatario);
        }
        
        // Crear dos solicitudes de prueba asociadas a la propiedad y al arrendatario (para "Ver solicitudes")
        Solicitud solicitud1 = new Solicitud();
        solicitud1.setFechaInicio("2025-04-10");
        solicitud1.setFechaFin("2025-04-15");
        solicitud1.setCantidadPer(2);
        solicitud1.setCostoTotal(250000 * 5);
        solicitud1.setStatus(Status.INACTIVE);
        solicitud1.setPropiedad(propiedad);
        solicitud1.setArrendatario(arrendatario);
        solicitudRepositorio.save(solicitud1);
        
        Solicitud solicitud2 = new Solicitud();
        solicitud2.setFechaInicio("2025-05-01");
        solicitud2.setFechaFin("2025-05-05");
        solicitud2.setCantidadPer(3);
        solicitud2.setCostoTotal(250000 * 4);
        solicitud2.setStatus(Status.INACTIVE);
        solicitud2.setPropiedad(propiedad);
        solicitud2.setArrendatario(arrendatario);
        solicitudRepositorio.save(solicitud2);
    }
    
    // ----- CASO: Solicitar arriendo (Crear solicitud) -----
    @Test
    public void testSave_Solicitud_Success() {
        // Arrange: Crear un DTO válido para solicitar arriendo
        SolicitudDto dto = new SolicitudDto();
        dto.setFechaInicio("2025-06-01");
        dto.setFechaFin("2025-06-05");
        dto.setCantidadPer(2);
        // Se asigna la propiedad (necesario que el DTO de propiedad no sea nulo)
        com.host_go.host_go.Dtos.PropiedadDto propiedadDto = new com.host_go.host_go.Dtos.PropiedadDto();
        propiedadDto.setPropiedadId(propiedad.getPropiedadId());
        dto.setPropiedad(propiedadDto);
        // Se asigna el arrendatario
        com.host_go.host_go.Dtos.ArrendatarioDto arrendatarioDto = new com.host_go.host_go.Dtos.ArrendatarioDto();
        arrendatarioDto.setArrendatarioId(arrendatario.getArrendatarioId());
        dto.setArrendatario(arrendatarioDto);
        
        // Act: Guardar la solicitud
        SolicitudDto saved = solicitudServicio.save(dto);
        
        // Assert: Se debe crear la solicitud y quedar en estado INACTIVE
        assertNotNull(saved);
        assertTrue(saved.getSolicitudId() > 0);
        assertEquals("2025-06-01", saved.getFechaInicio());
        assertEquals("2025-06-05", saved.getFechaFin());
        assertEquals(Status.INACTIVE, saved.getStatus(), "La solicitud debe quedar en estado INACTIVE al crearse");
        // El costoTotal se calcula automáticamente
        int expectedCosto = propiedad.getValorNoche() * (int)(LocalDate.parse("2025-06-05").toEpochDay() - LocalDate.parse("2025-06-01").toEpochDay());
        assertEquals(expectedCosto, saved.getCostoTotal());
    }
    
    @Test
    public void testSave_Solicitud_InvalidDates_ThrowsException() {
        // Arrange: Fecha fin anterior a fecha inicio
        SolicitudDto dto = new SolicitudDto();
        dto.setFechaInicio("2025-06-10");
        dto.setFechaFin("2025-06-05");
        dto.setCantidadPer(2);
        com.host_go.host_go.Dtos.PropiedadDto propiedadDto = new com.host_go.host_go.Dtos.PropiedadDto();
        propiedadDto.setPropiedadId(propiedad.getPropiedadId());
        dto.setPropiedad(propiedadDto);
        com.host_go.host_go.Dtos.ArrendatarioDto arrendatarioDto = new com.host_go.host_go.Dtos.ArrendatarioDto();
        arrendatarioDto.setArrendatarioId(arrendatario.getArrendatarioId());
        dto.setArrendatario(arrendatarioDto);
        
        // Act & Assert: Se espera excepción por fechas inválidas
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            solicitudServicio.save(dto);
        });
        assertEquals("La fecha fin no puede ser anterior a la fecha inicio", exception.getMessage());
    }
    
    @Test
    public void testSave_Solicitud_InvalidCapacity_ThrowsException() {
        // Arrange: Cantidad de personas mayor que la capacidad de la propiedad
        SolicitudDto dto = new SolicitudDto();
        dto.setFechaInicio("2025-06-01");
        dto.setFechaFin("2025-06-05");
        dto.setCantidadPer(10); // Capacidad de la propiedad es 6
        com.host_go.host_go.Dtos.PropiedadDto propiedadDto = new com.host_go.host_go.Dtos.PropiedadDto();
        propiedadDto.setPropiedadId(propiedad.getPropiedadId());
        dto.setPropiedad(propiedadDto);
        com.host_go.host_go.Dtos.ArrendatarioDto arrendatarioDto = new com.host_go.host_go.Dtos.ArrendatarioDto();
        arrendatarioDto.setArrendatarioId(arrendatario.getArrendatarioId());
        dto.setArrendatario(arrendatarioDto);
        
        // Act & Assert: Se espera excepción por exceso de capacidad
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            solicitudServicio.save(dto);
        });
        assertEquals("La cantidad de personas excede la capacidad de la propiedad", exception.getMessage());
    }
    
    // ----- CASO: Ver solicitudes de arriendo -----
    
    @Test
    public void testObtenerSolicitudesPorPropiedadYArrendador_Success() {
        // Act: Llamar al método con propiedad y arrendador correctos
        List<SolicitudDto> solicitudes = solicitudServicio.obtenerSolicitudesPorPropiedadYArrendador(propiedad.getPropiedadId(), arrendador.getArrendadorId());
        
        // Assert: Se deben recuperar 2 solicitudes (creadas en el setup)
        assertNotNull(solicitudes);
        assertEquals(2, solicitudes.size());
    }
    
    @Test
    public void testObtenerSolicitudesPorPropiedadYArrendador_PropertyNotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            solicitudServicio.obtenerSolicitudesPorPropiedadYArrendador(999L, arrendador.getArrendadorId());
        });
        assertEquals("Propiedad no encontrada", exception.getMessage());
    }
    
    @Test
    public void testObtenerSolicitudesPorPropiedadYArrendador_NoPermission() {
        Exception exception = assertThrows(SecurityException.class, () -> {
            solicitudServicio.obtenerSolicitudesPorPropiedadYArrendador(propiedad.getPropiedadId(), 999L);
        });
        assertEquals("No tienes permisos para ver estas solicitudes", exception.getMessage());
    }
    
    // ----- CASO: Aceptar solicitud de arriendo -----
    
    @Test
    public void testAceptarSolicitud_Success() {
        // Arrange: Crear una solicitud de prueba
        SolicitudDto solicitud = new SolicitudDto();
        solicitud.setFechaInicio("2025-04-10");
        solicitud.setFechaFin("2025-04-15");
        solicitud.setCantidadPer(2);
        solicitud.setPropiedad(new com.host_go.host_go.Dtos.PropiedadDto());
        solicitud.getPropiedad().setPropiedadId(propiedad.getPropiedadId());
        solicitud.setArrendatario(new com.host_go.host_go.Dtos.ArrendatarioDto());
        solicitud.getArrendatario().setArrendatarioId(arrendatario.getArrendatarioId());
        
        SolicitudDto savedSolicitud = solicitudServicio.save(solicitud);
        assertNotNull(savedSolicitud);
        assertEquals(Status.INACTIVE, savedSolicitud.getStatus(), "La solicitud debería quedar en estado INACTIVE al crearse");
        
        // Act: Aceptar la solicitud
        SolicitudDto acceptedSolicitud = solicitudServicio.aceptarSolicitud(savedSolicitud.getSolicitudId());
        
        // Assert: Verificar que el estado cambió a ACTIVE
        assertNotNull(acceptedSolicitud, "La solicitud aceptada no debe ser nula");
        assertEquals(Status.ACTIVE, acceptedSolicitud.getStatus(), "La solicitud debe quedar en estado ACTIVE al aceptarse");
    }
    
    @Test
    public void testAceptarSolicitud_InvalidState_ThrowsException() {
        // Arrange: Crear una solicitud de prueba
        SolicitudDto solicitud = new SolicitudDto();
        solicitud.setFechaInicio("2025-04-10");
        solicitud.setFechaFin("2025-04-15");
        solicitud.setCantidadPer(2);
        solicitud.setPropiedad(new com.host_go.host_go.Dtos.PropiedadDto());
        solicitud.getPropiedad().setPropiedadId(propiedad.getPropiedadId());
        solicitud.setArrendatario(new com.host_go.host_go.Dtos.ArrendatarioDto());
        solicitud.getArrendatario().setArrendatarioId(arrendatario.getArrendatarioId());
        
        SolicitudDto savedSolicitud = solicitudServicio.save(solicitud);
        // Primero, aceptamos la solicitud
        SolicitudDto accepted = solicitudServicio.aceptarSolicitud(savedSolicitud.getSolicitudId());
        assertEquals(Status.ACTIVE, accepted.getStatus(), "La solicitud ya debería estar en estado ACTIVE");
        
        // Act & Assert: Intentar aceptar nuevamente debe lanzar excepción
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            solicitudServicio.aceptarSolicitud(accepted.getSolicitudId());
        });
        assertEquals("La solicitud no se encuentra en estado INACTIVE y no se puede aceptar", exception.getMessage());
    }
    
    // ----- CASO: Cancelar solicitud de arriendo -----
    
    @Test
    public void testCancelarSolicitud_Success() {
        // Arrange: Crear una solicitud de prueba
        SolicitudDto solicitud = new SolicitudDto();
        solicitud.setFechaInicio("2025-04-10");
        solicitud.setFechaFin("2025-04-15");
        solicitud.setCantidadPer(2);
        solicitud.setPropiedad(new com.host_go.host_go.Dtos.PropiedadDto());
        solicitud.getPropiedad().setPropiedadId(propiedad.getPropiedadId());
        solicitud.setArrendatario(new com.host_go.host_go.Dtos.ArrendatarioDto());
        solicitud.getArrendatario().setArrendatarioId(arrendatario.getArrendatarioId());
        
        SolicitudDto savedSolicitud = solicitudServicio.save(solicitud);
        assertNotNull(savedSolicitud);
        assertEquals(Status.INACTIVE, savedSolicitud.getStatus(), "La solicitud debe estar en estado INACTIVE inicialmente");
        
        // Act: Cancelar la solicitud
        SolicitudDto canceledSolicitud = solicitudServicio.cancelarSolicitud(savedSolicitud.getSolicitudId());
        
        // Assert: Verificar que el estado cambió a DELETED
        assertNotNull(canceledSolicitud, "La solicitud cancelada no debe ser nula");
        assertEquals(Status.DELETED, canceledSolicitud.getStatus(), "La solicitud debe quedar en estado DELETED al cancelarse");
    }
    
    @Test
    public void testCancelarSolicitud_InvalidState_ThrowsException() {
        // Arrange: Crear una solicitud de prueba
        SolicitudDto solicitud = new SolicitudDto();
        solicitud.setFechaInicio("2025-04-10");
        solicitud.setFechaFin("2025-04-15");
        solicitud.setCantidadPer(2);
        solicitud.setPropiedad(new com.host_go.host_go.Dtos.PropiedadDto());
        solicitud.getPropiedad().setPropiedadId(propiedad.getPropiedadId());
        solicitud.setArrendatario(new com.host_go.host_go.Dtos.ArrendatarioDto());
        solicitud.getArrendatario().setArrendatarioId(arrendatario.getArrendatarioId());
        
        SolicitudDto savedSolicitud = solicitudServicio.save(solicitud);
        // Aceptamos la solicitud para que su estado sea ACTIVE
        SolicitudDto accepted = solicitudServicio.aceptarSolicitud(savedSolicitud.getSolicitudId());
        assertEquals(Status.ACTIVE, accepted.getStatus(), "La solicitud ya debería estar en estado ACTIVE");
        
        // Act & Assert: Intentar cancelarla cuando no está en INACTIVE debe lanzar excepción
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            solicitudServicio.cancelarSolicitud(accepted.getSolicitudId());
        });
        assertEquals("La solicitud no se encuentra en estado INACTIVE y no se puede cancelar", exception.getMessage());
    }
}
