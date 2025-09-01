package com.lms.educa.service;

import org.springframework.stereotype.Service;
import com.lms.educa.Entidades.*;
import java.util.List;

@Service
public class AdminFacade {
    private final UsuarioService usuarioService;
    private final MateriaService materiaService;
    private final ContenidoService contenidoService;
    private final ReporteService reporteService;

    public AdminFacade(UsuarioService usuarioService,
                      MateriaService materiaService,
                      ContenidoService contenidoService,
                      ReporteService reporteService) {
        this.usuarioService = usuarioService;
        this.materiaService = materiaService;
        this.contenidoService = contenidoService;
        this.reporteService = reporteService;
    }

    // Métodos de orquestación administrativa
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    public Materia crearMateria(Materia materia) {
        return materiaService.crearMateria(materia);
    }

    public Contenido crearContenido(Contenido contenido) {
        return contenidoService.crearContenido(contenido);
    }

    public Reporte crearReporte(Reporte reporte) {
        return reporteService.crearReporte(reporte);
    }

    // Ejemplo de métodos de consulta
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    public List<Materia> listarMaterias() {
        return materiaService.listarMaterias();
    }

    public List<Contenido> listarContenidos() {
        return contenidoService.listarContenidos();
    }

    public List<Reporte> listarReportes() {
        return reporteService.listarReportes();
    }
}
