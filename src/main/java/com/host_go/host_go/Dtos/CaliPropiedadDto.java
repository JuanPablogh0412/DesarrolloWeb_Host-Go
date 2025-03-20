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
    private long CaliPropiedad_id;
    private int Estrellas;
    private String Comentario;
    private PropiedadDto propiedad;
}
