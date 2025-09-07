package com.lms.educa.servicios;

import com.lms.educa.Entidades.Reporte;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReporteServicio {
    public Reporte crearReporte(Reporte reporte) {
        // Lógica para crear reporte
        return reporte;
    }
    public List<Reporte> listarReportes() {
        // Lógica para listar reportes
        return List.of();
    }
}
