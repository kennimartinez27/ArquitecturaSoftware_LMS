package com.lms.educa.servicios;

import com.lms.educa.Entidades.Contenido;
import com.lms.educa.repositorios.ContenidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ContenidoService {
    
    @Autowired
    private ContenidoRepository contenidoRepository;
    
    // Directorio donde se guardarán los archivos
    private final String uploadDir = "uploads/contenidos/";
    
    public ContenidoService() {
        // Crear el directorio si no existe
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de uploads", e);
        }
    }
    
    public Contenido crearContenido(Contenido contenido, MultipartFile archivo) throws IOException {
        if (archivo != null && !archivo.isEmpty()) {
            String nombreArchivo = guardarArchivo(archivo);
            contenido.setArchivo(nombreArchivo);
        }
        
        if (contenido.getEstado() == null || contenido.getEstado().isEmpty()) {
            contenido.setEstado("activo");
        }
        
        return contenidoRepository.save(contenido);
    }
    
    public List<Contenido> listarContenidos() {
        return contenidoRepository.findAll();
    }
    
    public List<Contenido> listarContenidosPorMateria(Long materiaId) {
        return contenidoRepository.findByMateriaId(materiaId);
    }
    
    public Contenido obtenerContenido(Long id) {
        return contenidoRepository.findById(id).orElse(null);
    }
    
    public Contenido actualizarContenido(Contenido contenido, MultipartFile archivo) throws IOException {
        Contenido contenidoExistente = contenidoRepository.findById(contenido.getId())
            .orElseThrow(() -> new RuntimeException("Contenido no encontrado"));
        
        contenidoExistente.setTitulo(contenido.getTitulo());
        contenidoExistente.setDescripcion(contenido.getDescripcion());
        contenidoExistente.setTipo(contenido.getTipo());
        contenidoExistente.setEstado(contenido.getEstado());
        
        if (archivo != null && !archivo.isEmpty()) {
            // Eliminar archivo anterior si existe
            if (contenidoExistente.getArchivo() != null) {
                eliminarArchivo(contenidoExistente.getArchivo());
            }
            // Guardar nuevo archivo
            String nombreArchivo = guardarArchivo(archivo);
            contenidoExistente.setArchivo(nombreArchivo);
        }
        
        return contenidoRepository.save(contenidoExistente);
    }
    
    public void eliminarContenido(Long id) {
        Contenido contenido = contenidoRepository.findById(id).orElse(null);
        if (contenido != null && contenido.getArchivo() != null) {
            eliminarArchivo(contenido.getArchivo());
        }
        contenidoRepository.deleteById(id);
    }
    
    private String guardarArchivo(MultipartFile archivo) throws IOException {
        // Generar nombre único para el archivo
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }
        String nombreUnico = UUID.randomUUID().toString() + extension;
        
        // Guardar archivo
        Path rutaArchivo = Paths.get(uploadDir + nombreUnico);
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        
        return nombreUnico;
    }
    
    private void eliminarArchivo(String nombreArchivo) {
        try {
            Path rutaArchivo = Paths.get(uploadDir + nombreArchivo);
            Files.deleteIfExists(rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al eliminar archivo: " + e.getMessage());
        }
    }
    
    public Path obtenerRutaArchivo(String nombreArchivo) {
        return Paths.get(uploadDir).resolve(nombreArchivo);
    }
}
