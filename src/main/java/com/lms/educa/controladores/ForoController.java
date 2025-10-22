package com.lms.educa.controladores;

import com.lms.educa.Entidades.Foro;
import com.lms.educa.Entidades.MensajeForo;
import com.lms.educa.servicios.ForoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foros")
@Tag(name = "Foros", description = "API para gestión de foros y mensajes")
@CrossOrigin(origins = "http://localhost:8080")
public class ForoController {
    
    @Autowired
    private ForoService foroService;
    
    /**
     * Crear un nuevo foro para una materia.
     */
    @Operation(summary = "Crear foro", description = "Crea un nuevo foro de discusión para una materia")
    @PostMapping("/crear")
    public ResponseEntity<?> crearForo(@RequestBody ForoDTO foroDTO) {
        try {
            Foro foro = foroService.crearForo(foroDTO.getTema(), foroDTO.getMateriaId());
            return ResponseEntity.ok(foro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Listar todos los foros de una materia.
     */
    @Operation(summary = "Listar foros por materia", description = "Obtiene todos los foros de una materia específica")
    @GetMapping("/materia/{materiaId}")
    public ResponseEntity<List<Foro>> listarForosPorMateria(@PathVariable("materiaId") Long materiaId) {
        List<Foro> foros = foroService.listarForosPorMateria(materiaId);
        return ResponseEntity.ok(foros);
    }
    
    /**
     * Obtener un foro específico.
     */
    @Operation(summary = "Obtener foro", description = "Obtiene un foro por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerForo(@PathVariable("id") Long id) {
        Foro foro = foroService.obtenerForo(id);
        if (foro == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foro);
    }
    
    /**
     * Eliminar un foro.
     */
    @Operation(summary = "Eliminar foro", description = "Elimina un foro y todos sus mensajes")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarForo(@PathVariable("id") Long id) {
        try {
            foroService.eliminarForo(id);
            return ResponseEntity.ok("Foro eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el foro: " + e.getMessage());
        }
    }
    
    /**
     * Crear un mensaje en un foro.
     */
    @Operation(summary = "Crear mensaje", description = "Agrega un nuevo mensaje a un foro")
    @PostMapping("/{foroId}/mensajes")
    public ResponseEntity<?> crearMensaje(
            @PathVariable("foroId") Long foroId,
            @RequestBody MensajeDTO mensajeDTO) {
        try {
            MensajeForo mensaje = foroService.crearMensaje(
                foroId, 
                mensajeDTO.getUsuarioId(), 
                mensajeDTO.getContenido()
            );
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Listar todos los mensajes de un foro.
     */
    @Operation(summary = "Listar mensajes de foro", description = "Obtiene todos los mensajes de un foro ordenados por fecha")
    @GetMapping("/{foroId}/mensajes")
    public ResponseEntity<List<MensajeForo>> listarMensajes(@PathVariable("foroId") Long foroId) {
        List<MensajeForo> mensajes = foroService.listarMensajesPorForo(foroId);
        return ResponseEntity.ok(mensajes);
    }
    
    /**
     * Eliminar un mensaje.
     */
    @Operation(summary = "Eliminar mensaje", description = "Elimina un mensaje de un foro")
    @DeleteMapping("/mensajes/{id}")
    public ResponseEntity<?> eliminarMensaje(@PathVariable("id") Long id) {
        try {
            foroService.eliminarMensaje(id);
            return ResponseEntity.ok("Mensaje eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el mensaje: " + e.getMessage());
        }
    }
    
    // DTOs internos
    public static class ForoDTO {
        private String tema;
        private Long materiaId;
        
        public String getTema() {
            return tema;
        }
        
        public void setTema(String tema) {
            this.tema = tema;
        }
        
        public Long getMateriaId() {
            return materiaId;
        }
        
        public void setMateriaId(Long materiaId) {
            this.materiaId = materiaId;
        }
    }
    
    public static class MensajeDTO {
        private Long usuarioId;
        private String contenido;
        
        public Long getUsuarioId() {
            return usuarioId;
        }
        
        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
        }
        
        public String getContenido() {
            return contenido;
        }
        
        public void setContenido(String contenido) {
            this.contenido = contenido;
        }
    }
}
