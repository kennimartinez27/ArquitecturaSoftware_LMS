package com.lms.educa.Entidades;

import jakarta.persistence.*;
import lombok.Data;


import java.util.Set;

@Data
@Entity
@Table(name = "Usuario")
public abstract class Usuario {
    public Usuario() {}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(nullable = false, length = 100)
    private String contrasena;

    @Column(nullable = false, length = 50)
    private String rol;

    // Relaciones
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<MensajeForo> mensajesForo;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<Reporte> reportes;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<EvaluacionUsuario> evaluacionesUsuario;

    @ManyToMany(mappedBy = "usuarios")
    private Set<Materia> materias;

    public Usuario(Long id, String nombre, String correo, String contrasena, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }
}
