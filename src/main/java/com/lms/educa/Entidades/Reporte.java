package com.lms.educa.Entidades;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Reporte")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String motivo;

    @Column(nullable = false, length = 50)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "contenido_id")
    private Contenido contenido;
}
