package com.lms.educa.Entidades;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "MensajeForo")
public class MensajeForo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "foro_id", nullable = false)
    private Foro foro;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fecha;

    // Getters y setters
    // ...
}
