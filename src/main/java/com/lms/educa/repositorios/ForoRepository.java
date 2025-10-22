package com.lms.educa.repositorios;

import com.lms.educa.Entidades.Foro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForoRepository extends JpaRepository<Foro, Long> {
    List<Foro> findByMateriaId(Long materiaId);
}
