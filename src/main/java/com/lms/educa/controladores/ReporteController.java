package com.lms.educa.controladores;

import com.lms.educa.Entidades.Reporte;
import com.lms.educa.servicios.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "API para gestión de reportes de contenido inapropiado")
@CrossOrigin(origins = "http://localhost:8080")
public class ReporteController {
    
    @Autowired
    private ReporteService reporteService;
    
    /**
     * Crear un reporte de contenido inapropiado
     */
    @Operation(summary = "Crear reporte", description = "Crea un reporte de contenido inapropiado")
    @PostMapping("/crear")
    public ResponseEntity<?> crearReporte(@RequestBody ReporteDTO reporteDTO) {
        try {
            Reporte reporte = reporteService.crearReporte(
                reporteDTO.getUsuarioId(),
                reporteDTO.getContenidoId(),
                reporteDTO.getMotivo()
            );
            return ResponseEntity.ok(reporte);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Listar todos los reportes
     */
    @Operation(summary = "Listar reportes", description = "Obtiene todos los reportes")
    @GetMapping("/listar")
    public ResponseEntity<List<Reporte>> listarReportes() {
        List<Reporte> reportes = reporteService.listarReportes();
        return ResponseEntity.ok(reportes);
    }
    
    /**
     * Listar reportes por estado
     */
    @Operation(summary = "Listar reportes por estado", description = "Obtiene reportes filtrados por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reporte>> listarReportesPorEstado(@PathVariable("estado") String estado) {
        List<Reporte> reportes = reporteService.listarReportesPorEstado(estado);
        return ResponseEntity.ok(reportes);
    }
    
    /**
     * Listar reportes de un contenido
     */
    @Operation(summary = "Listar reportes de contenido", description = "Obtiene reportes de un contenido específico")
    @GetMapping("/contenido/{contenidoId}")
    public ResponseEntity<List<Reporte>> listarReportesPorContenido(@PathVariable("contenidoId") Long contenidoId) {
        List<Reporte> reportes = reporteService.listarReportesPorContenido(contenidoId);
        return ResponseEntity.ok(reportes);
    }
    
    /**
     * Listar reportes de un usuario
     */
    @Operation(summary = "Listar reportes de usuario", description = "Obtiene reportes realizados por un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Reporte>> listarReportesPorUsuario(@PathVariable("usuarioId") Long usuarioId) {
        List<Reporte> reportes = reporteService.listarReportesPorUsuario(usuarioId);
        return ResponseEntity.ok(reportes);
    }
    
    /**
     * Obtener un reporte específico
     */
    @Operation(summary = "Obtener reporte", description = "Obtiene un reporte por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerReporte(@PathVariable("id") Long id) {
        Reporte reporte = reporteService.obtenerReporte(id);
        if (reporte == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reporte);
    }
    
    /**
     * Cambiar el estado de un reporte
     */
    @Operation(summary = "Cambiar estado de reporte", description = "Actualiza el estado de un reporte")
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable("id") Long id,
            @RequestBody EstadoDTO estadoDTO) {
        try {
            Reporte reporte = reporteService.cambiarEstado(id, estadoDTO.getEstado());
            return ResponseEntity.ok(reporte);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Eliminar un reporte
     */
    @Operation(summary = "Eliminar reporte", description = "Elimina un reporte")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReporte(@PathVariable("id") Long id) {
        try {
            reporteService.eliminarReporte(id);
            return ResponseEntity.ok("Reporte eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el reporte: " + e.getMessage());
        }
    }
    
    /**
     * Obtener estadísticas de reportes
     */
    @Operation(summary = "Estadísticas de reportes", description = "Obtiene conteo de reportes por estado")
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("pendientes", reporteService.contarReportesPendientes());
        stats.put("total", reporteService.listarReportes().size());
        return ResponseEntity.ok(stats);
    }
    
    // DTOs internos
    public static class ReporteDTO {
        private Long usuarioId;
        private Long contenidoId;
        private String motivo;
        
        public Long getUsuarioId() {
            return usuarioId;
        }
        
        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
        }
        
        public Long getContenidoId() {
            return contenidoId;
        }
        
        public void setContenidoId(Long contenidoId) {
            this.contenidoId = contenidoId;
        }
        
        public String getMotivo() {
            return motivo;
        }
        
        public void setMotivo(String motivo) {
            this.motivo = motivo;
        }
    }
    
    public static class EstadoDTO {
        private String estado;
        
        public String getEstado() {
            return estado;
        }
        
        public void setEstado(String estado) {
            this.estado = estado;
        }
    }
}
