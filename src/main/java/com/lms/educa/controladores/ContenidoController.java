package com.lms.educa.controladores;

import com.lms.educa.Entidades.Contenido;
import com.lms.educa.Entidades.Materia;
import com.lms.educa.servicios.ContenidoService;
import com.lms.educa.servicios.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Controlador para la gestión de contenidos.
 * Permite subir, descargar y gestionar archivos de materias.
 */
@Tag(name = "Contenidos", description = "Operaciones sobre contenidos de materias")
@RestController
@RequestMapping("/api/contenidos")
public class ContenidoController {
    
    @Autowired
    private ContenidoService contenidoService;
    
    @Autowired
    private MateriaService materiaService;
    
    /**
     * Crea un nuevo contenido con archivo opcional.
     */
    @Operation(summary = "Crear contenido", description = "Crea un nuevo contenido con archivo opcional")
    @PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearContenido(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tipo") String tipo,
            @RequestParam("materiaId") Long materiaId,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo) {
        try {
            Materia materia = materiaService.obtenerMateria(materiaId);
            if (materia == null) {
                return ResponseEntity.badRequest().body("Materia no encontrada");
            }
            
            Contenido contenido = new Contenido();
            contenido.setTitulo(titulo);
            contenido.setDescripcion(descripcion);
            contenido.setTipo(tipo);
            contenido.setMateria(materia);
            
            Contenido contenidoCreado = contenidoService.crearContenido(contenido, archivo);
            return ResponseEntity.ok(contenidoCreado);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al guardar el archivo: " + e.getMessage());
        }
    }
    
    /**
     * Lista todos los contenidos.
     */
    @Operation(summary = "Listar contenidos", description = "Devuelve la lista de todos los contenidos")
    @GetMapping("/listar")
    public List<Contenido> listarContenidos() {
        return contenidoService.listarContenidos();
    }
    
    /**
     * Lista los contenidos de una materia específica.
     */
    @Operation(summary = "Listar contenidos por materia", description = "Devuelve los contenidos de una materia")
    @GetMapping("/materia/{materiaId}")
    public List<Contenido> listarContenidosPorMateria(@PathVariable("materiaId") Long materiaId) {
        return contenidoService.listarContenidosPorMateria(materiaId);
    }
    
    /**
     * Obtiene un contenido por su identificador.
     */
    @Operation(summary = "Obtener contenido", description = "Devuelve un contenido por ID")
    @GetMapping("/{id}")
    public Contenido obtenerContenido(@PathVariable("id") Long id) {
        return contenidoService.obtenerContenido(id);
    }
    
    /**
     * Descarga un archivo de contenido.
     */
    @Operation(summary = "Descargar archivo", description = "Descarga el archivo de un contenido")
    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable("id") Long id) {
        try {
            Contenido contenido = contenidoService.obtenerContenido(id);
            if (contenido == null || contenido.getArchivo() == null) {
                return ResponseEntity.notFound().build();
            }
            
            Path rutaArchivo = contenidoService.obtenerRutaArchivo(contenido.getArchivo());
            Resource recurso = new UrlResource(rutaArchivo.toUri());
            
            if (recurso.exists() && recurso.isReadable()) {
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + contenido.getTitulo() + "\"")
                    .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Actualiza un contenido existente.
     */
    @Operation(summary = "Actualizar contenido", description = "Actualiza los datos de un contenido")
    @PutMapping(value = "/actualizar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> actualizarContenido(
            @PathVariable("id") Long id,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tipo") String tipo,
            @RequestParam("estado") String estado,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo) {
        try {
            Contenido contenido = contenidoService.obtenerContenido(id);
            if (contenido == null) {
                return ResponseEntity.notFound().build();
            }
            
            contenido.setTitulo(titulo);
            contenido.setDescripcion(descripcion);
            contenido.setTipo(tipo);
            contenido.setEstado(estado);
            
            Contenido contenidoActualizado = contenidoService.actualizarContenido(contenido, archivo);
            return ResponseEntity.ok(contenidoActualizado);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al actualizar el archivo: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un contenido por su identificador.
     */
    @Operation(summary = "Eliminar contenido", description = "Elimina un contenido por ID")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarContenido(@PathVariable("id") Long id) {
        try {
            contenidoService.eliminarContenido(id);
            return ResponseEntity.ok("Contenido eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar contenido: " + e.getMessage());
        }
    }
}
