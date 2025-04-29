package com.host_go.host_go.Dtos;

import java.util.List;

import com.host_go.host_go.modelos.Foto;
import com.host_go.host_go.modelos.Propiedad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropiedadResumenDto {
    private Long propiedadId;
    private String nombre;
    private String departamento;
    private String municipio;
    private int valorNoche;
     private List<Foto> fotos;

    // Constructor para facilitar el mapeo
    public PropiedadResumenDto(Propiedad propiedad) {
        this.propiedadId = propiedad.getPropiedadId();
        this.nombre = propiedad.getNombre();
        this.departamento = propiedad.getDepartamento();
        this.municipio = propiedad.getMunicipio();
        this.valorNoche = propiedad.getValorNoche();
        this.fotos = propiedad.getFotos();
    }
}