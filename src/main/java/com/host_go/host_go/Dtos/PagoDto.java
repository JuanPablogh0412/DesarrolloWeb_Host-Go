package com.host_go.host_go.Dtos;
import com.host_go.host_go.modelos.Bancos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoDto {
    private long pagoId;
    private Bancos banco;
    private long valor;
    private long numCuenta;
    private SolicitudDto solicitud;
}
