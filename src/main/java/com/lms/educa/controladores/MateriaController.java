package com.lms.educa.controladores;

import com.lms.educa.Entidades.Materia;
import com.lms.educa.servicios.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/materias")
public class MateriaController {
    @Autowired
    private MateriaService materiaService;

    @PostMapping("/crear")
    public Materia crearMateria(@RequestBody Materia materia) {
        return materiaService.crearMateria(materia);
    }

    @GetMapping("/listar")
    public List<Materia> listarMaterias() {
        return materiaService.listarMaterias();
    }

    @GetMapping("/{id}")
    public Materia obtenerMateria(@PathVariable Long id) {
        return materiaService.obtenerMateria(id);
    }

    @PutMapping("/actualizar")
    public Materia actualizarMateria(@RequestBody Materia materia) {
        return materiaService.actualizarMateria(materia);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarMateria(@PathVariable Long id) {
        materiaService.eliminarMateria(id);
    }
}
