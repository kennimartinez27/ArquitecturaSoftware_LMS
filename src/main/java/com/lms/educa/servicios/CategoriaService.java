package com.lms.educa.servicios;

import com.lms.educa.Entidades.Categoria;
import com.lms.educa.repositorios.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria obtenerCategoria(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public Categoria actualizarCategoria(Categoria categoria) {
        // Obtener la categoría existente
        Categoria categoriaExistente = categoriaRepository.findById(categoria.getId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        // Actualizar solo el nombre
        categoriaExistente.setNombre(categoria.getNombre());
        
        return categoriaRepository.save(categoriaExistente);
    }

    public void eliminarCategoria(Long id) {
        // Verificar si la categoría tiene materias asociadas
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        if (categoria.getMaterias() != null && !categoria.getMaterias().isEmpty()) {
            throw new RuntimeException("No se puede eliminar la categoría porque tiene " + 
                categoria.getMaterias().size() + " materia(s) asociada(s)");
        }
        
        categoriaRepository.deleteById(id);
    }
}
