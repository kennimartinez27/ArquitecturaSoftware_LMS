package com.lms.educa.servicios;

import com.lms.educa.Entidades.Usuario;
import com.lms.educa.Factory.UsuarioFactory;
import com.lms.educa.repositorios.UsuarioRepository;
import com.lms.educa.Entidades.Materia;
import com.lms.educa.Entidades.Estudiante;
import com.lms.educa.repositorios.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    public Usuario crearUsuario(UsuarioFactory factory, String nombre, String correo, String contrasena) {
        Usuario usuario = factory.crearUsuario(null, nombre, correo, contrasena);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Asocia una materia a un estudiante.
     * @param estudianteId ID del estudiante
     * @param materiaId ID de la materia
     * @return true si la asociación fue exitosa, false si no
     */
    public boolean asociarMateriaAEstudiante(Long estudianteId, Long materiaId) {
        Usuario usuario = usuarioRepository.findById(estudianteId).orElse(null);
        if (usuario == null || !(usuario instanceof Estudiante)) {
            return false;
        }
        Materia materia = materiaRepository.findById(materiaId).orElse(null);
        if (materia == null) {
            return false;
        }
        Estudiante estudiante = (Estudiante) usuario;
        if (estudiante.getMaterias() == null) {
            estudiante.setMaterias(new java.util.HashSet<>());
        }
        estudiante.getMaterias().add(materia);
        if (materia.getUsuarios() == null) {
            materia.setUsuarios(new java.util.HashSet<>());
        }
        materia.getUsuarios().add(estudiante);
        usuarioRepository.save(estudiante);
        materiaRepository.save(materia);
        return true;
    }
}
