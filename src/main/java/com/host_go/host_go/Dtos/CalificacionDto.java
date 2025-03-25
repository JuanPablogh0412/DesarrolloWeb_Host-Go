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
    private long calificacionId;
    private int estrellas;
    private String comentario;
    private CuentaDto cuenta;
}
