package com.host_go.host_go.controladores;

import java.util.List;

import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Dtos.SolicitudDto;
import com.host_go.host_go.Repositorios.ArrendatarioRepositorio;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import com.host_go.host_go.Servicios.SolicitudServicio;
import com.host_go.host_go.modelos.Arrendatario;
import com.host_go.host_go.modelos.Cuenta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping(value = "/Solicitud")
public class SolicitudControlador {

    @Autowired
    private SolicitudServicio SolicitudServicio;

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private ArrendatarioRepositorio arrendatarioRepositorio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SolicitudDto> get (){
        return SolicitudServicio.get();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SolicitudDto get(@PathVariable Long id){
        return SolicitudServicio.get(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SolicitudDto save(@RequestBody SolicitudDto SolicitudDto) throws ValidationException{
        return SolicitudServicio.save(SolicitudDto);
    }

    @CrossOrigin
    @PutMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public SolicitudDto update(@RequestBody SolicitudDto SolicitudDto) throws ValidationException{
        return SolicitudServicio.update(SolicitudDto);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id){
        SolicitudServicio.delete(id);
    }

    @CrossOrigin
    @GetMapping(value = "/misSolicitudes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> obtenerMisSolicitudes(@AuthenticationPrincipal UserDetails userDetails) {
        // Buscar la cuenta y luego el arrendatario asociado a esa cuenta
        Cuenta cuenta = cuentaRepositorio.findByUsuario(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Arrendatario arrendatario = arrendatarioRepositorio.findByCuenta(cuenta)
                .orElseThrow(() -> new IllegalArgumentException("Arrendatario no encontrado"));
        
        List<SolicitudDto> solicitudes = SolicitudServicio.obtenerSolicitudesPorArrendatario(arrendatario.getArrendatarioId());
        return ResponseEntity.ok(solicitudes);
    }


    @CrossOrigin
    @GetMapping("path")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @CrossOrigin
    @GetMapping(value = "/propiedad/{propiedadId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SolicitudDto> obtenerSolicitudesPorPropiedad(@PathVariable Long propiedadId, 
                                                            @RequestParam Long arrendadorId) {
        return SolicitudServicio.obtenerSolicitudesPorPropiedadYArrendador(propiedadId, arrendadorId);
    }

    @CrossOrigin
    @PutMapping(value = "/aceptar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SolicitudDto aceptarSolicitud(@PathVariable Long id) {
        return SolicitudServicio.aceptarSolicitud(id);
    }

    @CrossOrigin
    @PutMapping(value = "/cancelar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SolicitudDto cancelarSolicitud(@PathVariable Long id) {
        return SolicitudServicio.cancelarSolicitud(id);
    }
    
}
