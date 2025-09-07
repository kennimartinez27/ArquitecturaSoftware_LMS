package com.lms.educa.controladores;

import com.lms.educa.Entidades.Usuario;
import com.lms.educa.Factory.AdministradorFactory;
import com.lms.educa.Factory.EstudianteFactory;
import com.lms.educa.Factory.ProfesorFactory;
import com.lms.educa.Factory.UsuarioFactory;
import com.lms.educa.servicios.UsuarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public static class UsuarioDTO {
        public String tipo;
        public String nombre;
        public String correo;
        public String contrasena;
    }

    @PostMapping("/crear")
    public Usuario crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        String tipoNormalizado = usuarioDTO.tipo.trim().toLowerCase();
        System.out.println("Tipo recibido: '" + usuarioDTO.tipo + "' (normalizado: '" + tipoNormalizado + "')");
        UsuarioFactory factory;
        switch (tipoNormalizado) {
            case "administrador":
                factory = new AdministradorFactory();
                break;
            case "profesor":
                factory = new ProfesorFactory();
                break;
            case "estudiante":
                factory = new EstudianteFactory();
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuario no válido: " + usuarioDTO.tipo);
        }
        return usuarioService.crearUsuario(factory, usuarioDTO.nombre, usuarioDTO.correo, usuarioDTO.contrasena);
    }

    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }
}
