package com.lms.educa.Entidades;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "Evaluacion")
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaLimite;

    private Integer puntajeMaximo;

    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL)
    private Set<EvaluacionUsuario> evaluacionesUsuario;
}
