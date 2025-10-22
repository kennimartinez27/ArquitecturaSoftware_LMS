package com.lms.educa.repositorios;

import com.lms.educa.Entidades.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenidoRepository extends JpaRepository<Contenido, Long> {
    List<Contenido> findByMateriaId(Long materiaId);
    List<Contenido> findByTipo(String tipo);
    List<Contenido> findByEstado(String estado);
}
