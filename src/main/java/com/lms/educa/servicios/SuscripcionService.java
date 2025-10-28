package com.lms.educa.servicios;

import com.lms.educa.Entidades.Estudiante;
import com.lms.educa.Entidades.Suscripcion;
import com.lms.educa.repositorios.SuscripcionRepository;
import com.lms.educa.repositorios.UsuarioRepository;
import com.mercadopago.resources.payment.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SuscripcionService {

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Crear una nueva suscripción pendiente
     */
    @Transactional
    public Suscripcion crearSuscripcionPendiente(Long estudianteId, String preferenciaId, BigDecimal monto) {
        Estudiante estudiante = (Estudiante) usuarioRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setEstudiante(estudiante);
        suscripcion.setPreferenciaId(preferenciaId);
        suscripcion.setMontoPagado(monto);
        suscripcion.setEstadoPago("pending");
        suscripcion.setEstadoSuscripcion("inactiva");
        suscripcion.setFechaPago(LocalDateTime.now());

        return suscripcionRepository.save(suscripcion);
    }

    /**
     * Activar una suscripción cuando el pago es aprobado
     */
    @Transactional
    public Suscripcion activarSuscripcion(String paymentId, Payment payment) {
        // Buscar la suscripción por external reference (estudiante-ID)
        String externalRef = payment.getExternalReference();
        Long estudianteId = Long.parseLong(externalRef.replace("estudiante-", ""));

        // Buscar si ya existe una suscripción con este paymentId
        Optional<Suscripcion> suscripcionExistente = suscripcionRepository.findByPaymentId(paymentId);
        if (suscripcionExistente.isPresent()) {
            return suscripcionExistente.get();
        }

        // Crear nueva suscripción o actualizar la existente
        Suscripcion suscripcion = suscripcionRepository.findByEstudianteId(estudianteId)
                .stream()
                .filter(s -> "pending".equals(s.getEstadoPago()))
                .findFirst()
                .orElse(new Suscripcion());

        if (suscripcion.getId() == null) {
            Estudiante estudiante = (Estudiante) usuarioRepository.findById(estudianteId)
                    .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
            suscripcion.setEstudiante(estudiante);
        }

        suscripcion.setPaymentId(paymentId);
        suscripcion.setEstadoPago(payment.getStatus());
        suscripcion.setEstadoSuscripcion("activa");
        suscripcion.setMontoPagado(payment.getTransactionAmount());
        suscripcion.setMetodoPago(payment.getPaymentMethodId());
        suscripcion.setFechaPago(LocalDateTime.now());

        return suscripcionRepository.save(suscripcion);
    }

    /**
     * Verificar si un estudiante tiene suscripción activa
     */
    public boolean tienesSuscripcionActiva(Long estudianteId) {
        Optional<Suscripcion> suscripcion = suscripcionRepository.findSuscripcionActivaByEstudianteId(estudianteId);
        return suscripcion.isPresent();
    }

    /**
     * Obtener suscripción activa de un estudiante
     */
    public Optional<Suscripcion> obtenerSuscripcionActiva(Long estudianteId) {
        return suscripcionRepository.findSuscripcionActivaByEstudianteId(estudianteId);
    }

    /**
     * Obtener todas las suscripciones de un estudiante
     */
    public List<Suscripcion> obtenerSuscripcionesPorEstudiante(Long estudianteId) {
        return suscripcionRepository.findByEstudianteId(estudianteId);
    }

    /**
     * Obtener todas las suscripciones
     */
    public List<Suscripcion> obtenerTodasLasSuscripciones() {
        return suscripcionRepository.findAllByOrderByFechaPagoDesc();
    }

    /**
     * Obtener cantidad de suscripciones activas
     */
    public Long contarSuscripcionesActivas() {
        return suscripcionRepository.countSuscripcionesActivas();
    }

    /**
     * Desactivar una suscripción (por ejemplo, por reembolso)
     */
    @Transactional
    public Suscripcion desactivarSuscripcion(Long suscripcionId) {
        Suscripcion suscripcion = suscripcionRepository.findById(suscripcionId)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));
        
        suscripcion.setEstadoSuscripcion("inactiva");
        return suscripcionRepository.save(suscripcion);
    }
}
