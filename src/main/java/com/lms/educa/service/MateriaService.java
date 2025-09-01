package com.lms.educa.service;

import com.lms.educa.Entidades.Materia;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MateriaService {
    public Materia crearMateria(Materia materia) {
        // Lógica para crear materia
        return materia;
    }
    public List<Materia> listarMaterias() {
        // Lógica para listar materias
        return List.of();
    }
}
