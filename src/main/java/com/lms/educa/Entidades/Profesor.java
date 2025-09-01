package com.lms.educa.Entidades;

public class Profesor extends Usuario {
    public Profesor(Long id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena, "Profesor");
    }
}
