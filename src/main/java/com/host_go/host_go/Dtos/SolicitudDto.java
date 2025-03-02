package com.host_go.host_go.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDto {
private long Solicitud_id;
    private String FechaInicio;
    private String FechaFin;
    private int CantidadPer;
    private int CostoTotal;

    private PropiedadDto propiedad;
    private ArrendatarioDto arrendatario;
}
