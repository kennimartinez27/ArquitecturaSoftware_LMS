package com.lms.educa.Entidades;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "Categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private Set<Materia> materias;
}
