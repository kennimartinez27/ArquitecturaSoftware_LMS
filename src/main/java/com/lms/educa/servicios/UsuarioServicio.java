package com.lms.educa.servicios;

import com.lms.educa.Entidades.Usuario;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {
    public Usuario crearUsuario(Usuario usuario) {
        // Lógica para crear usuario
        return usuario;
    }
    public List<Usuario> listarUsuarios() {
        // Lógica para listar usuarios
        return List.of();
    }
}
