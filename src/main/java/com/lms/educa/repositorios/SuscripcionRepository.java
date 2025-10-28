package com.lms.educa.repositorios;

import com.lms.educa.Entidades.Estudiante;
import com.lms.educa.Entidades.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {
    
    // Buscar suscripciones por estudiante
    List<Suscripcion> findByEstudiante(Estudiante estudiante);
    
    // Buscar suscripciones por estudiante ID
    List<Suscripcion> findByEstudianteId(Long estudianteId);
    
    // Buscar suscripción activa de un estudiante
    @Query("SELECT s FROM Suscripcion s WHERE s.estudiante.id = :estudianteId AND s.estadoSuscripcion = 'activa' AND s.estadoPago = 'approved'")
    Optional<Suscripcion> findSuscripcionActivaByEstudianteId(@Param("estudianteId") Long estudianteId);
    
    // Buscar por preferencia ID
    Optional<Suscripcion> findByPreferenciaId(String preferenciaId);
    
    // Buscar por payment ID
    Optional<Suscripcion> findByPaymentId(String paymentId);
    
    // Contar suscripciones activas
    @Query("SELECT COUNT(s) FROM Suscripcion s WHERE s.estadoSuscripcion = 'activa' AND s.estadoPago = 'approved'")
    Long countSuscripcionesActivas();
    
    // Obtener todas las suscripciones ordenadas por fecha
    List<Suscripcion> findAllByOrderByFechaPagoDesc();
}
