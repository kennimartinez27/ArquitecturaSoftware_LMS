package com.lms.educa.controladores;


import com.lms.educa.Entidades.Materia;
import com.lms.educa.servicios.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Controlador para la gestión de materias.
 * Utiliza el patrón Facade a través de los servicios para orquestar operaciones administrativas.
 */
@Tag(name = "Materias", description = "Operaciones sobre materias (Facade)")
@RestController
@RequestMapping("/materias")
public class MateriaController {
    @Autowired
    private MateriaService materiaService;


    /**
     * Crea una nueva materia.
     */
    @Operation(summary = "Crear materia", description = "Crea una materia usando el servicio especializado (Facade)")
    @PostMapping("/crear")
    public Materia crearMateria(@RequestBody Materia materia) {
        return materiaService.crearMateria(materia);
    }


    /**
     * Lista todas las materias registradas.
     */
    @Operation(summary = "Listar materias", description = "Devuelve la lista de materias")
    @GetMapping("/listar")
    public List<Materia> listarMaterias() {
        return materiaService.listarMaterias();
    }


    /**
     * Obtiene una materia por su identificador.
     */
    @Operation(summary = "Obtener materia", description = "Devuelve una materia por ID")
    @GetMapping("/{id}")
    public Materia obtenerMateria(@PathVariable Long id) {
        return materiaService.obtenerMateria(id);
    }


    /**
     * Actualiza una materia existente.
     */
    @Operation(summary = "Actualizar materia", description = "Actualiza los datos de una materia")
    @PutMapping("/actualizar")
    public Materia actualizarMateria(@RequestBody Materia materia) {
        return materiaService.actualizarMateria(materia);
    }


    /**
     * Elimina una materia por su identificador.
     */
    @Operation(summary = "Eliminar materia", description = "Elimina una materia por ID")
    @DeleteMapping("/eliminar/{id}")
    public void eliminarMateria(@PathVariable Long id) {
        materiaService.eliminarMateria(id);
    }
}
