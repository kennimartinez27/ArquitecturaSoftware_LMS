package com.lms.educa.Factory;

import com.lms.educa.Entidades.Usuario;

public interface UsuarioFactory {
    Usuario crearUsuario(Long id, String nombre, String correo, String contraseña);
}
