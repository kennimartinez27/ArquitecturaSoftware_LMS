package com.lms.educa.servicios;

import com.lms.educa.Entidades.Materia;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MateriaServicio {
    public Materia crearMateria(Materia materia) {
        // Lógica para crear materia
        return materia;
    }
    public List<Materia> listarMaterias() {
        // Lógica para listar materias
        return List.of();
    }
}
