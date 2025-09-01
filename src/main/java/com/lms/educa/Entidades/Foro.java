package com.lms.educa.Entidades;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Foro")
public class Foro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String tema;

    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @OneToMany(mappedBy = "foro", cascade = CascadeType.ALL)
    private Set<MensajeForo> mensajesForo;

    // Getters y setters
    // ...
}
