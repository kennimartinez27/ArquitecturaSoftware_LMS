package com.lms.educa.controladores;


import com.lms.educa.Entidades.Usuario;
import com.lms.educa.servicios.AdminFacade;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controlador para la gestión de usuarios.
 * Aplica el patrón Factory Method para instanciar subtipos de Usuario según el rol.
 */
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios y roles (Factory Method)")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final AdminFacade adminFacade;

    public UsuarioController(AdminFacade adminFacade) {
        this.adminFacade = adminFacade;
    }

    public static class UsuarioDTO {
        public String tipo;
        public String nombre;
        public String correo;
        public String contrasena;
    }


    /**
     * Crea un usuario según el tipo especificado usando el patrón Factory Method.
     * @param usuarioDTO Datos del usuario y tipo de rol
     * @return Usuario creado
     */
    @Operation(summary = "Crear usuario", description = "Crea un usuario según el rol usando Factory Method")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "500", description = "Tipo de usuario no válido")
    })
    @PostMapping("/crear")
    public Usuario crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        String tipoNormalizado = usuarioDTO.tipo.trim().toLowerCase();
        System.out.println("Tipo recibido: '" + usuarioDTO.tipo + "' (normalizado: '" + tipoNormalizado + "')");
        return adminFacade.crearUsuario(tipoNormalizado, usuarioDTO.nombre, usuarioDTO.correo, usuarioDTO.contrasena);
    }


    /**
     * Lista todos los usuarios registrados.
     */
    @Operation(summary = "Listar usuarios", description = "Devuelve la lista de usuarios")
    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return adminFacade.listarUsuarios();
    }
}
