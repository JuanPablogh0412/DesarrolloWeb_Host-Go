package com.host_go.host_go.Excepciones;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor



public class MensajeError {
    private String mensaje;
    private Date fecha;
}
