package com.lms.educa.controladores;


import com.lms.educa.Entidades.Categoria;
import com.lms.educa.servicios.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Controlador para la gestión de categorías.
 * Utiliza el patrón Facade a través de los servicios para orquestar operaciones administrativas.
 */
@Tag(name = "Categorías", description = "Operaciones sobre categorías (Facade)")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;


    /**
     * Crea una nueva categoría.
     */
    @Operation(summary = "Crear categoría", description = "Crea una categoría usando el servicio especializado (Facade)")
    @PostMapping("/crear")
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        return categoriaService.crearCategoria(categoria);
    }


    /**
     * Lista todas las categorías registradas.
     */
    @Operation(summary = "Listar categorías", description = "Devuelve la lista de categorías")
    @GetMapping("/listar")
    public List<Categoria> listarCategorias() {
        return categoriaService.listarCategorias();
    }


    /**
     * Obtiene una categoría por su identificador.
     */
    @Operation(summary = "Obtener categoría", description = "Devuelve una categoría por ID")
    @GetMapping("/{id}")
    public Categoria obtenerCategoria(@PathVariable Long id) {
        return categoriaService.obtenerCategoria(id);
    }


    /**
     * Actualiza una categoría existente.
     */
    @Operation(summary = "Actualizar categoría", description = "Actualiza los datos de una categoría")
    @PutMapping("/actualizar")
    public Categoria actualizarCategoria(@RequestBody Categoria categoria) {
        return categoriaService.actualizarCategoria(categoria);
    }


    /**
     * Elimina una categoría por su identificador.
     */
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría por ID")
    @DeleteMapping("/eliminar/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
    }
}
