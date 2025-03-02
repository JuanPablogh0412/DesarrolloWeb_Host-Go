package com.host_go.host_go.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionDto {
private long Calificacion_id;
    private int Estrellas;
    private String Comentario;
    private CuentaDto cuenta;
}
