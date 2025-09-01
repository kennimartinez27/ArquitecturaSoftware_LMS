package com.lms.educa.Factory;

import com.lms.educa.Entidades.Profesor;
import com.lms.educa.Entidades.Usuario;

public class ProfesorFactory implements UsuarioFactory {
    @Override
    public Usuario crearUsuario(Long id, String nombre, String correo, String contraseña) {
        return new Profesor(id, nombre, correo, contraseña);
    }
}
