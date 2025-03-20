package com.host_go.host_go.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrendatarioDto {
    private long Arrendatario_id;
    private Integer Cedula;
    private String Nombre;
    private String Apellido;
    private String Correo;
    private long Telefono;
    private CuentaDto cuenta;

}
