package com.lms.educa.Factory;

import com.lms.educa.Entidades.Usuario;

/**
 * Interfaz de fábrica para la creación de usuarios por rol.
 * Aplica el patrón Factory Method para instanciar subtipos de Usuario (Administrador, Profesor, Estudiante).
 * Permite agregar nuevos roles sin modificar el resto del sistema.
 */
public interface UsuarioFactory {
    /**
     * Crea una instancia de Usuario según el rol.
     * @param id Identificador único
     * @param nombre Nombre del usuario
     * @param correo Correo electrónico
     * @param contraseña Contraseña
     * @return Instancia de Usuario específica del rol
     */
    Usuario crearUsuario(Long id, String nombre, String correo, String contraseña);
}
