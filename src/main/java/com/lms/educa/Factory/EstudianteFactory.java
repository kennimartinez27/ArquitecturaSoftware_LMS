package com.lms.educa.Factory;

import com.lms.educa.Entidades.Estudiante;
import com.lms.educa.Entidades.Usuario;

public class EstudianteFactory implements UsuarioFactory {
    @Override
    public Usuario crearUsuario(Long id, String nombre, String correo, String contraseña) {
        return new Estudiante(id, nombre, correo, contraseña);
    }
}
