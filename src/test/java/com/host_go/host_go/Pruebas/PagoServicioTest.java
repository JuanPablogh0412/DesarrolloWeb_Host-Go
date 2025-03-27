package com.host_go.host_go.Pruebas;

import static org.junit.jupiter.api.Assertions.*;

import com.host_go.host_go.Dtos.PagoDto;
import com.host_go.host_go.Dtos.SolicitudDto;
import com.host_go.host_go.Servicios.PagoServicio;
import com.host_go.host_go.Servicios.SolicitudServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class PagoServicioTest {

    @Autowired
    private PagoServicio pagoServicio;

    @Autowired
    private SolicitudServicio solicitudService;
    
    private SolicitudDto solicitudDtoVigente;
    private SolicitudDto solicitudDtoVencida;
    
    @BeforeEach
    public void setup() {
        // Crear una solicitud vigente (fechaFin futura)
        SolicitudDto dtoVigente = new SolicitudDto();
        dtoVigente.setFechaInicio(LocalDate.now().plusDays(1).toString());
        dtoVigente.setFechaFin(LocalDate.now().plusDays(6).toString());
        dtoVigente.setCantidadPer(2);
        // Se asigna un DTO de propiedad válido; se asume que existe una propiedad con ID 1.
        com.host_go.host_go.Dtos.PropiedadDto propDto = new com.host_go.host_go.Dtos.PropiedadDto();
        propDto.setPropiedadId(1L);
        dtoVigente.setPropiedad(propDto);
        // Se asigna un DTO de arrendatario válido; se asume que existe un arrendatario con ID 1.
        com.host_go.host_go.Dtos.ArrendatarioDto arrDto = new com.host_go.host_go.Dtos.ArrendatarioDto();
        arrDto.setArrendatarioId(1L);
        dtoVigente.setArrendatario(arrDto);
        
        solicitudDtoVigente = solicitudService.save(dtoVigente);
        
        // Crear una solicitud vencida (fechaFin en el pasado)
        SolicitudDto dtoVencida = new SolicitudDto();
        dtoVencida.setFechaInicio(LocalDate.now().minusDays(10).toString());
        dtoVencida.setFechaFin(LocalDate.now().minusDays(5).toString());
        dtoVencida.setCantidadPer(2);
        com.host_go.host_go.Dtos.PropiedadDto propDto2 = new com.host_go.host_go.Dtos.PropiedadDto();
        propDto2.setPropiedadId(1L);
        dtoVencida.setPropiedad(propDto2);
        com.host_go.host_go.Dtos.ArrendatarioDto arrDto2 = new com.host_go.host_go.Dtos.ArrendatarioDto();
        arrDto2.setArrendatarioId(1L);
        dtoVencida.setArrendatario(arrDto2);
        
        solicitudDtoVencida = solicitudService.save(dtoVencida);
    }
    
    // ----- Caso: Pagar arriendo exitoso -----
    @Test
    public void testSave_Pago_Success() {
        PagoDto pagoDto = new PagoDto();
        // Se asigna la solicitud vigente (solo se requiere el ID)
        SolicitudDto solDto = new SolicitudDto();
        solDto.setSolicitudId(solicitudDtoVigente.getSolicitudId());
        pagoDto.setSolicitud(solDto);
        // Asignar otros datos de pago
        pagoDto.setBanco(com.host_go.host_go.modelos.Bancos.BANCOLOMBIA);
        pagoDto.setNumCuenta(123456789);
        
        // Act: Realizar el pago
        PagoDto pagoGuardado = pagoServicio.save(pagoDto);
        
        // Assert: Verificar que el pago se haya creado y que el valor se iguale al costoTotal de la solicitud
        assertNotNull(pagoGuardado);
        assertTrue(pagoGuardado.getPagoId() > 0);
        assertEquals(solicitudDtoVigente.getCostoTotal(), pagoGuardado.getValor());
    }
    
    // ----- Caso: Pagar arriendo para solicitud vencida -----
    @Test
    public void testSave_Pago_SolicitudVencida_ThrowsException() {
        PagoDto pagoDto = new PagoDto();
        // Usar la solicitud vencida
        SolicitudDto solDto = new SolicitudDto();
        solDto.setSolicitudId(solicitudDtoVencida.getSolicitudId());
        pagoDto.setSolicitud(solDto);
        pagoDto.setBanco(com.host_go.host_go.modelos.Bancos.BBVA);
        pagoDto.setNumCuenta(987654321);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pagoServicio.save(pagoDto);
        });
        assertEquals("La solicitud está vencida y no se puede pagar", exception.getMessage());
    }
}
