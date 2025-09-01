package com.lms.educa.Entidades;

public class Estudiante extends Usuario {
    public Estudiante(Long id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena, "Estudiante");
    }

}
