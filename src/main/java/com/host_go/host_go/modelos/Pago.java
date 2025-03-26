package com.host_go.host_go.modelos;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("deprecation")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "status = 'ACTIVE'") // Filtra solo los activos
@SQLDelete(sql = "UPDATE foto SET status = 'DELETED' WHERE pagoId = ?") // Soft delete
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pagoId;
    private Bancos banco;
    private long valor;
    private long numCuenta;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @OneToOne
    @JoinColumn(name = "solicitudId")
    private Solicitud solicitud;
}
