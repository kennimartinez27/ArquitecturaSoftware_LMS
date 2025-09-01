package com.lms.educa.Factory;

import com.lms.educa.Entidades.Administrador;
import com.lms.educa.Entidades.Usuario;

public class AdministradorFactory implements UsuarioFactory {
    @Override
    public Usuario crearUsuario(Long id, String nombre, String correo, String contraseña) {
        return new Administrador(id, nombre, correo, contraseña);
    }
}
