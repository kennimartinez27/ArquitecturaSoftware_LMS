package com.lms.educa.repositorios;

import com.lms.educa.Entidades.MensajeForo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeForoRepository extends JpaRepository<MensajeForo, Long> {
    List<MensajeForo> findByForoIdOrderByFechaAsc(Long foroId);
    List<MensajeForo> findByForoIdOrderByFechaDesc(Long foroId);
}
