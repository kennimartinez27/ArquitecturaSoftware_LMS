package com.lms.educa.servicios;

import com.lms.educa.Entidades.Materia;
import com.lms.educa.repositorios.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MateriaService {
    @Autowired
    private MateriaRepository materiaRepository;

    public Materia crearMateria(Materia materia) {
        return materiaRepository.save(materia);
    }

    public List<Materia> listarMaterias() {
        return materiaRepository.findAll();
    }

    public Materia obtenerMateria(Long id) {
        return materiaRepository.findById(id).orElse(null);
    }

    public Materia actualizarMateria(Materia materia) {
        // Obtener la materia existente
        Materia materiaExistente = materiaRepository.findById(materia.getId())
            .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        
        // Actualizar solo los campos necesarios
        materiaExistente.setNombre(materia.getNombre());
        materiaExistente.setCategoria(materia.getCategoria());
        
        return materiaRepository.save(materiaExistente);
    }

    public void eliminarMateria(Long id) {
        materiaRepository.deleteById(id);
    }
}
