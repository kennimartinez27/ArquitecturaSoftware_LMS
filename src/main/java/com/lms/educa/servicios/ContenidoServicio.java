package com.lms.educa.servicios;

import com.lms.educa.Entidades.Contenido;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ContenidoServicio {
    public Contenido crearContenido(Contenido contenido) {
        // Lógica para crear contenido
        return contenido;
    }
    public List<Contenido> listarContenidos() {
        // Lógica para listar contenidos
        return List.of();
    }
}
