    
package com.lms.educa.servicios;

import org.springframework.stereotype.Service;
import com.lms.educa.Entidades.*;
import java.util.List;



@Service
public class AdminFacade {
    private final UsuarioService usuarioService;
    private final MateriaService materiaService;

    public AdminFacade(UsuarioService usuarioService,
            MateriaService materiaService,

            CategoriaService categoriaService) {
        this.usuarioService = usuarioService;
        this.materiaService = materiaService;
    }
                                               
    // Métodos de orquestación administrativa
    /**
     * Crea un usuario según el tipo usando el patrón Factory Method y delega en UsuarioService.
     */
    public Usuario crearUsuario(String tipo, String nombre, String correo, String contrasena) {
        String tipoNormalizado = tipo.trim().toLowerCase();
        com.lms.educa.Factory.UsuarioFactory factory;
        switch (tipoNormalizado) {
            case "administrador":
                factory = new com.lms.educa.Factory.AdministradorFactory();
                break;
            case "profesor":
                factory = new com.lms.educa.Factory.ProfesorFactory();
                break;
            case "estudiante":
                factory = new com.lms.educa.Factory.EstudianteFactory();
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuario no válido: " + tipo);
        }
        return usuarioService.crearUsuario(factory, nombre, correo, contrasena);
    }

    

    public Materia crearMateria(Materia materia) {
        return materiaService.crearMateria(materia);
    }



    // Ejemplo de métodos de consulta

    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    public List<Materia> listarMaterias() {
        return materiaService.listarMaterias();
    }
}
