package com.lms.educa.Entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Contenido")
public class Contenido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 255)
    private String archivo;

    @Column(length = 50)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @Column(nullable = false, length = 50)
    private String tipo;

    // Getters y setters
    // ...
}
