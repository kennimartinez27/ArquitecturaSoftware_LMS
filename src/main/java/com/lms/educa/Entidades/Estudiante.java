
package com.lms.educa.Entidades;

import com.lms.educa.interfaces.Observer;
import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Estudiante extends Usuario implements Observer {
    // Relaciones específicas de Estudiante
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<MensajeForo> mensajesForo;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<Reporte> reportes;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<EvaluacionUsuario> evaluacionesUsuario;

    @ManyToMany(mappedBy = "usuarios")
    private Set<Materia> materias;
    @Override
    public void update(String evento, Object data) {
        // Aquí puedes implementar la lógica de notificación (UI, email, push)
        System.out.println("Estudiante notificado: " + evento);
    }
    public Estudiante() {
        super();
    }
    public Estudiante(Long id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena, "Estudiante");
    }

}
