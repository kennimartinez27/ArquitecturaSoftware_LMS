package com.lms.educa.service;

import com.lms.educa.Entidades.Reporte;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReporteService {
    public Reporte crearReporte(Reporte reporte) {
        // Lógica para crear reporte
        return reporte;
    }
    public List<Reporte> listarReportes() {
        // Lógica para listar reportes
        return List.of();
    }
}
