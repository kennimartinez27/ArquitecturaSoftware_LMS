package com.lms.educa.repositorios;

import com.lms.educa.Entidades.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByEstado(String estado);
    List<Reporte> findByContenidoId(Long contenidoId);
    List<Reporte> findByUsuarioId(Long usuarioId);
    List<Reporte> findByEstadoOrderByIdDesc(String estado);
}
