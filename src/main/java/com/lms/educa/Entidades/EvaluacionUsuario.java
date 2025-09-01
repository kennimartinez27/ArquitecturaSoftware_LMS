package com.lms.educa.Entidades;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Evaluacion_Usuario")
public class EvaluacionUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    private Integer calificacion;

    @Column(columnDefinition = "date")
    private LocalDate fecha;
}
