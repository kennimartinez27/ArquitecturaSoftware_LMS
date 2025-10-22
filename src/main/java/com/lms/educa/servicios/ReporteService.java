package com.lms.educa.servicios;

import com.lms.educa.Entidades.Contenido;
import com.lms.educa.Entidades.Reporte;
import com.lms.educa.Entidades.Usuario;
import com.lms.educa.repositorios.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteService {
    
    @Autowired
    private ReporteRepository reporteRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ContenidoService contenidoService;
    
    /**
     * Crear un nuevo reporte de contenido inapropiado
     */
    public Reporte crearReporte(Long usuarioId, Long contenidoId, String motivo) {
        Usuario usuario = usuarioService.obtenerUsuario(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        Contenido contenido = contenidoService.obtenerContenido(contenidoId);
        if (contenido == null) {
            throw new RuntimeException("Contenido no encontrado");
        }
        
        Reporte reporte = new Reporte();
        reporte.setUsuario(usuario);
        reporte.setContenido(contenido);
        reporte.setMotivo(motivo);
        reporte.setEstado("Pendiente");
        
        return reporteRepository.save(reporte);
    }
    
    /**
     * Listar todos los reportes
     */
    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }
    
    /**
     * Listar reportes por estado
     */
    public List<Reporte> listarReportesPorEstado(String estado) {
        return reporteRepository.findByEstadoOrderByIdDesc(estado);
    }
    
    /**
     * Listar reportes de un contenido específico
     */
    public List<Reporte> listarReportesPorContenido(Long contenidoId) {
        return reporteRepository.findByContenidoId(contenidoId);
    }
    
    /**
     * Listar reportes hechos por un usuario
     */
    public List<Reporte> listarReportesPorUsuario(Long usuarioId) {
        return reporteRepository.findByUsuarioId(usuarioId);
    }
    
    /**
     * Obtener un reporte específico
     */
    public Reporte obtenerReporte(Long id) {
        return reporteRepository.findById(id).orElse(null);
    }
    
    /**
     * Cambiar el estado de un reporte
     */
    public Reporte cambiarEstado(Long reporteId, String nuevoEstado) {
        Reporte reporte = obtenerReporte(reporteId);
        if (reporte == null) {
            throw new RuntimeException("Reporte no encontrado");
        }
        
        reporte.setEstado(nuevoEstado);
        return reporteRepository.save(reporte);
    }
    
    /**
     * Eliminar un reporte
     */
    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }
    
    /**
     * Contar reportes pendientes
     */
    public long contarReportesPendientes() {
        return reporteRepository.findByEstado("Pendiente").size();
    }
}
