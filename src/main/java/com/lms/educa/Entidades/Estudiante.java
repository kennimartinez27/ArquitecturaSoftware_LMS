package com.lms.educa.Entidades;

import com.lms.educa.interfaces.Observer;

public class Estudiante extends Usuario implements Observer {
    @Override
    public void update(String evento, Object data) {
        // Aquí puedes implementar la lógica de notificación (UI, email, push)
        System.out.println("Estudiante notificado: " + evento);
    }
    public Estudiante(Long id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena, "Estudiante");
    }

}
