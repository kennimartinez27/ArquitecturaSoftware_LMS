package com.lms.educa.servicios;

import com.lms.educa.Entidades.Foro;
import com.lms.educa.Entidades.Materia;
import com.lms.educa.Entidades.MensajeForo;
import com.lms.educa.Entidades.Usuario;
import com.lms.educa.repositorios.ForoRepository;
import com.lms.educa.repositorios.MensajeForoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForoService {
    
    @Autowired
    private ForoRepository foroRepository;
    
    @Autowired
    private MensajeForoRepository mensajeForoRepository;
    
    @Autowired
    private MateriaService materiaService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    // CRUD de Foros
    
    public Foro crearForo(String tema, Long materiaId) {
        Materia materia = materiaService.obtenerMateria(materiaId);
        if (materia == null) {
            throw new RuntimeException("Materia no encontrada");
        }
        
        Foro foro = new Foro();
        foro.setTema(tema);
        foro.setMateria(materia);
        
        return foroRepository.save(foro);
    }
    
    public List<Foro> listarForosPorMateria(Long materiaId) {
        return foroRepository.findByMateriaId(materiaId);
    }
    
    public Foro obtenerForo(Long id) {
        return foroRepository.findById(id).orElse(null);
    }
    
    public void eliminarForo(Long id) {
        foroRepository.deleteById(id);
    }
    
    // CRUD de Mensajes
    
    public MensajeForo crearMensaje(Long foroId, Long usuarioId, String contenido) {
        Foro foro = obtenerForo(foroId);
        if (foro == null) {
            throw new RuntimeException("Foro no encontrado");
        }
        
        Usuario usuario = usuarioService.obtenerUsuario(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        MensajeForo mensaje = new MensajeForo();
        mensaje.setForo(foro);
        mensaje.setUsuario(usuario);
        mensaje.setContenido(contenido);
        mensaje.setFecha(LocalDateTime.now());
        
        return mensajeForoRepository.save(mensaje);
    }
    
    public List<MensajeForo> listarMensajesPorForo(Long foroId) {
        return mensajeForoRepository.findByForoIdOrderByFechaAsc(foroId);
    }
    
    public void eliminarMensaje(Long id) {
        mensajeForoRepository.deleteById(id);
    }
}
