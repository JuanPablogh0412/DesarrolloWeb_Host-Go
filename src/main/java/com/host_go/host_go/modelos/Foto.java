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
@SQLDelete(sql = "UPDATE foto SET status = 'DELETED' WHERE foto_id = ?") // Soft delete
public class Foto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long foto_id;
    private String url;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    
    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;
}
