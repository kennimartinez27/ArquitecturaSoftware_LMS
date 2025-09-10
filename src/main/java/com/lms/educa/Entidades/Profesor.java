package com.lms.educa.Entidades;

import com.lms.educa.interfaces.Observer;
import jakarta.persistence.Entity;

@Entity
public class Profesor extends Usuario implements Observer {
    @Override
    public void update(String evento, Object data) {
        // Aquí puedes implementar la lógica de notificación (UI, email, push)
        System.out.println("Profesor notificado: " + evento);
    }
    public Profesor() {
        super();
    }
    public Profesor(Long id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena, "Profesor");
    }
}
