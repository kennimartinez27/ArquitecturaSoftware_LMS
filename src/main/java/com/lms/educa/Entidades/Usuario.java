package com.lms.educa.Entidades;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Inheritance(strategy = jakarta.persistence.InheritanceType.JOINED)
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

 

    public Usuario(Long id, String nombre, String correo, String contrasena, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }
}
