package com.lms.educa.Entidades;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

import com.lms.educa.interfaces.Subject;

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
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Categoria categoria;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<Contenido> contenidos;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<Foro> foros;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<Evaluacion> evaluaciones;

    @ManyToMany
    @JoinTable(
        name = "Usuario_Materia",
        joinColumns = @JoinColumn(name = "materia_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Set<Usuario> usuarios;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Materia materia = (Materia) o;
        return id != null && id.equals(materia.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    // Getters y setters
    // ...
}
