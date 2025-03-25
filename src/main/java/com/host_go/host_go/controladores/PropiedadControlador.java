package com.host_go.host_go.controladores;

import java.util.List;
import java.util.Map;

import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Dtos.PropiedadDto;
import com.host_go.host_go.Dtos.PropiedadResumenDto;
import com.host_go.host_go.Repositorios.ArrendadorRepositorio;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import com.host_go.host_go.Servicios.PropiedadServicio;
import com.host_go.host_go.modelos.Arrendador;
import com.host_go.host_go.modelos.Cuenta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping(value = "/Propiedad")
public class PropiedadControlador {

    @Autowired
    private PropiedadServicio PropiedadServicio;
    @Autowired
    private CuentaRepositorio cuentaRepositorio;
    @Autowired
    private ArrendadorRepositorio arrendadorRepositorio;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PropiedadDto> get (){
        return PropiedadServicio.get();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PropiedadDto get(@PathVariable Long id){
        return PropiedadServicio.get(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PropiedadDto save(@RequestBody PropiedadDto PropiedadDto) throws ValidationException{
        return PropiedadServicio.save(PropiedadDto);
    }

    @CrossOrigin
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody PropiedadDto propiedadDto) {
        try {
            PropiedadDto propiedadActualizada = PropiedadServicio.update(propiedadDto);
            return ResponseEntity.ok(propiedadActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .internalServerError()
                .body(Map.of("error", "Error al actualizar la propiedad"));
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id){
        PropiedadServicio.delete(id);
    }

    @GetMapping(value = "/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PropiedadDto> buscarPropiedades(
     @RequestParam(required = false) String nombre,
     @RequestParam(required = false) String ubicacion,
     @RequestParam(defaultValue = "0") int capacidad) {
        return PropiedadServicio.buscarPropiedades(nombre, ubicacion, capacidad);
    }

    @GetMapping(value = "/misPropiedades", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMisPropiedades(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            Cuenta cuenta = cuentaRepositorio.findByUsuario(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            Arrendador arrendador = arrendadorRepositorio.findByCuenta(cuenta)
                .orElseThrow(() -> new IllegalArgumentException("Arrendador no encontrado"));

            List<PropiedadResumenDto> propiedades = PropiedadServicio.obtenerPropiedadesPorArrendador(arrendador.getArrendadorId());
            return ResponseEntity.ok(propiedades);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
    
}
