package com.lms.educa.Entidades;

import com.lms.educa.interfaces.Observer;

public class Administrador extends Usuario implements Observer {
    @Override
    public void update(String evento, Object data) {
        // Aquí puedes implementar la lógica de notificación (UI, email, push)
        System.out.println("Administrador notificado: " + evento);
    }
    public Administrador(Long id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena, "Administrador");
    }
    
}
