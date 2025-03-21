package com.host_go.host_go.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaliPropiedadDto {
    private long caliPropiedad_id;
    private int estrellas;
    private String comentario;
    private PropiedadDto propiedad;
}
