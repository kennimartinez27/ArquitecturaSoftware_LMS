package com.lms.educa.Entidades;

public class Administrador extends Usuario {
    public Administrador(Long id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena, "Administrador");
    }

}
