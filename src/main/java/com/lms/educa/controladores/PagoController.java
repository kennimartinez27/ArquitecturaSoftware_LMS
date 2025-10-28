package com.lms.educa.controladores;

import com.lms.educa.Entidades.Suscripcion;
import com.lms.educa.Entidades.Usuario;
import com.lms.educa.repositorios.UsuarioRepository;
import com.lms.educa.servicios.MercadoPagoService;
import com.lms.educa.servicios.SuscripcionService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Pagos y Suscripciones", description = "Gestión de pagos con Mercado Pago y suscripciones")
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private SuscripcionService suscripcionService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${mercadopago.precio.suscripcion}")
    private String precioSuscripcion;

    /**
     * Iniciar proceso de pago para suscripción
     */
    @Operation(summary = "Crear preferencia de pago", description = "Crea una preferencia de Mercado Pago para suscripción")
    @PostMapping("/crear-preferencia/{estudianteId}")
    public ResponseEntity<?> crearPreferencia(@PathVariable Long estudianteId) {
        try {
            System.out.println("=== Iniciando creación de preferencia para estudiante: " + estudianteId + " ===");
            
            // Verificar si el estudiante ya tiene suscripción activa
            if (suscripcionService.tienesSuscripcionActiva(estudianteId)) {
                System.out.println("El estudiante ya tiene suscripción activa");
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Ya tienes una suscripción activa");
                response.put("suscripcionActiva", true);
                return ResponseEntity.badRequest().body(response);
            }

            // Obtener información del estudiante
            System.out.println("Buscando estudiante con ID: " + estudianteId);
            Usuario estudiante = usuarioRepository.findById(estudianteId)
                    .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + estudianteId));
            
            System.out.println("Estudiante encontrado: " + estudiante.getNombre() + " - " + estudiante.getCorreo());

            // Crear preferencia de pago
            System.out.println("Creando preferencia de pago en Mercado Pago...");
            Preference preference = mercadoPagoService.crearPreferenciaSuscripcion(
                    estudianteId, 
                    estudiante.getNombre(), 
                    estudiante.getCorreo()
            );
            
            System.out.println("Preferencia creada con ID: " + preference.getId());

            // Crear suscripción pendiente
            System.out.println("Creando suscripción pendiente en BD...");
            suscripcionService.crearSuscripcionPendiente(
                    estudianteId, 
                    preference.getId(), 
                    preference.getItems().get(0).getUnitPrice()
            );

            // Devolver datos necesarios para el frontend
            Map<String, Object> response = new HashMap<>();
            response.put("preferenceId", preference.getId());
            response.put("initPoint", preference.getInitPoint());
            response.put("sandboxInitPoint", preference.getSandboxInitPoint());
            
            System.out.println("Preferencia creada exitosamente");
            return ResponseEntity.ok(response);

        } catch (MPException e) {
            System.err.println("Error de Mercado Pago: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error de Mercado Pago: " + e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (MPApiException e) {
            System.err.println("Error de API Mercado Pago: " + e.getMessage());
            System.err.println("API Response: " + e.getApiResponse());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error de API Mercado Pago: " + e.getMessage());
            error.put("apiResponse", String.valueOf(e.getApiResponse()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error inesperado: " + e.getMessage());
            error.put("type", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Webhook para recibir notificaciones de Mercado Pago
     */
    @Operation(summary = "Webhook de Mercado Pago", description = "Recibe notificaciones de pagos")
    @PostMapping("/webhook")
    public ResponseEntity<?> webhookMercadoPago(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("Webhook recibido: " + payload);

            String type = (String) payload.get("type");
            
            if ("payment".equals(type)) {
                Map<String, Object> data = (Map<String, Object>) payload.get("data");
                String paymentIdStr = (String) data.get("id");
                Long paymentId = Long.parseLong(paymentIdStr);

                // Obtener información del pago
                Payment payment = mercadoPagoService.obtenerPago(paymentId);

                // Si el pago fue aprobado, activar suscripción
                if (mercadoPagoService.isPagoAprobado(payment)) {
                    suscripcionService.activarSuscripcion(String.valueOf(paymentId), payment);
                    System.out.println("Suscripción activada para payment: " + paymentId);
                }
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            System.err.println("Error procesando webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Verificar estado de suscripción de un estudiante
     */
    @Operation(summary = "Verificar suscripción", description = "Verifica si un estudiante tiene suscripción activa")
    @GetMapping("/verificar-suscripcion/{estudianteId}")
    public ResponseEntity<?> verificarSuscripcion(@PathVariable Long estudianteId) {
        Map<String, Object> response = new HashMap<>();
        
        boolean tieneSuscripcion = suscripcionService.tienesSuscripcionActiva(estudianteId);
        response.put("tieneSuscripcion", tieneSuscripcion);

        if (tieneSuscripcion) {
            Optional<Suscripcion> suscripcion = suscripcionService.obtenerSuscripcionActiva(estudianteId);
            suscripcion.ifPresent(s -> {
                response.put("fechaPago", s.getFechaPago());
                response.put("monto", s.getMontoPagado());
                response.put("metodoPago", s.getMetodoPago());
            });
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Obtener todas las suscripciones (solo para administradores)
     */
    @Operation(summary = "Listar suscripciones", description = "Obtiene todas las suscripciones registradas")
    @GetMapping("/suscripciones")
    public ResponseEntity<List<Suscripcion>> listarSuscripciones() {
        List<Suscripcion> suscripciones = suscripcionService.obtenerTodasLasSuscripciones();
        return ResponseEntity.ok(suscripciones);
    }

    /**
     * Obtener suscripciones de un estudiante específico
     */
    @Operation(summary = "Suscripciones de estudiante", description = "Obtiene las suscripciones de un estudiante")
    @GetMapping("/suscripciones/estudiante/{estudianteId}")
    public ResponseEntity<List<Suscripcion>> suscripcionesPorEstudiante(@PathVariable Long estudianteId) {
        List<Suscripcion> suscripciones = suscripcionService.obtenerSuscripcionesPorEstudiante(estudianteId);
        return ResponseEntity.ok(suscripciones);
    }

    /**
     * Obtener estadísticas de suscripciones
     */
    @Operation(summary = "Estadísticas de suscripciones", description = "Obtiene contadores y estadísticas")
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("suscripcionesActivas", suscripcionService.contarSuscripcionesActivas());
        stats.put("precioSuscripcion", precioSuscripcion);
        return ResponseEntity.ok(stats);
    }
}
