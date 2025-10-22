package com.lms.educa.servicios;

import com.lms.educa.Entidades.Usuario;
import com.lms.educa.Factory.UsuarioFactory;
import com.lms.educa.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(UsuarioFactory factory, String nombre, String correo, String contrasena) {
        Usuario usuario = factory.crearUsuario(null, nombre, correo, contrasena);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario actualizarUsuario(Usuario usuario) {
        // Obtener el usuario existente
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Actualizar solo los campos necesarios
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setCorreo(usuario.getCorreo());
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            usuarioExistente.setContrasena(usuario.getContrasena());
        }
        
        return usuarioRepository.save(usuarioExistente);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
