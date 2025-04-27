package com.host_go.host_go.controladores;

import java.util.List;

import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.host_go.host_go.Dtos.CaliPropiedadDto;
import com.host_go.host_go.Servicios.CaliPropiedadServicio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping(value = "/CaliPropiedad")
public class CaliPropiedadControlador {

    

    

    @Autowired
    private CaliPropiedadServicio CaliPropiedadServicio;
    

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CaliPropiedadDto> get (){
        return CaliPropiedadServicio.get();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CaliPropiedadDto get(@PathVariable Long id){
        return CaliPropiedadServicio.get(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CaliPropiedadDto save(@RequestBody CaliPropiedadDto CaliPropiedadDto) throws ValidationException{
        return CaliPropiedadServicio.save(CaliPropiedadDto);
    }

    @CrossOrigin
    @PutMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public CaliPropiedadDto update(@RequestBody CaliPropiedadDto CaliPropiedadDto) throws ValidationException{
        return CaliPropiedadServicio.update(CaliPropiedadDto);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long id){
        CaliPropiedadServicio.delete(id);
    }

    //Backend calificar propiedad
    @CrossOrigin
    @PostMapping(value = "/calificarProp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calificarPropiedad(
        @RequestParam int estrellas,
        @RequestParam String comentario,
        @RequestParam String nombrePropiedad,
        @RequestParam String ubicacion
    ){
        if(estrellas<=0 || comentario.isEmpty() || nombrePropiedad.isEmpty() || ubicacion.isEmpty()){
            return ResponseEntity.badRequest().body("Todos los elementos son obligatorios");
        }
        return CaliPropiedadServicio.calificarPropiedad(estrellas, comentario, nombrePropiedad, ubicacion);
    }
    @GetMapping(value = "/porPropiedad", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CaliPropiedadDto> getByNombreyUbicacion(
        @RequestParam String nombrePropiedad,
        @RequestParam String ubicacion
    ) {
        return CaliPropiedadServicio.obtenerComentariosporPropiedad(nombrePropiedad, ubicacion);
    }
    
}
    

