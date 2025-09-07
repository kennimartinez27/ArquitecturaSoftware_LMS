package com.lms.educa.Entidades;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

import com.lms.educa.interfaces.Subject;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "Materia")
public class Materia extends Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL)
    private Set<Contenido> contenidos;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL)
    private Set<Foro> foros;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL)
    private Set<Evaluacion> evaluaciones;

    @ManyToMany
    @JoinTable(
        name = "Usuario_Materia",
        joinColumns = @JoinColumn(name = "materia_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> usuarios;

    // Getters y setters
    // ...
}
