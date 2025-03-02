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
@Where(clause = "status = 0")
@SQLDelete(sql = "UPDATE application SET status = 1 WHERE id = ?")
public class Foto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Foto_id;
    private String Url;

    @ManyToOne
    @JoinColumn(name = "Propiedad_id")
    private Propiedad propiedad;
}
