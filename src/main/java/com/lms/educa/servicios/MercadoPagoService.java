package com.lms.educa.servicios;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    @Value("${mercadopago.precio.suscripcion}")
    private BigDecimal precioSuscripcion;

    @Value("${mercadopago.currency}")
    private String currency;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    /**
     * Crear una preferencia de pago para la suscripción
     */
    public Preference crearPreferenciaSuscripcion(Long estudianteId, String estudianteNombre, String estudianteEmail) 
            throws MPException, MPApiException {
        
        System.out.println("Creando preferencia con datos:");
        System.out.println("- Estudiante ID: " + estudianteId);
        System.out.println("- Nombre: " + estudianteNombre);
        System.out.println("- Email: " + estudianteEmail);
        System.out.println("- Precio: " + precioSuscripcion);
        System.out.println("- Moneda: " + currency);
        
        // Validar email
        if (estudianteEmail == null || estudianteEmail.trim().isEmpty() || !estudianteEmail.contains("@")) {
            throw new IllegalArgumentException("Email inválido: " + estudianteEmail);
        }
        
        // Crear el item de la suscripción
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id("suscripcion-examenes-" + estudianteId)
                .title("Suscripción para Descargar Exámenes")
                .description("Acceso único para descargar archivos de tipo examen")
                .pictureUrl("https://www.example.com/icon-examen.png")
                .categoryId("education")
                .quantity(1)
                .currencyId(currency)
                .unitPrice(precioSuscripcion)
                .build();

        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        // URLs de retorno (puedes ajustarlas según tu frontend)
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:8080/estudiante/dashboard.html?payment=success")
                .failure("http://localhost:8080/estudiante/dashboard.html?payment=failure")
                .pending("http://localhost:8080/estudiante/dashboard.html?payment=pending")
                .build();

        // IMPORTANTE: Para PSE en Colombia, NO configurar paymentMethods
        // Dejar que Mercado Pago use la configuración por defecto de la cuenta
        // Si se configura paymentMethods de forma incorrecta, puede limitar los métodos disponibles

        // Crear la preferencia
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                // NO agregar .paymentMethods() para permitir todos los métodos disponibles
                .externalReference("estudiante-" + estudianteId)
                .payer(com.mercadopago.client.preference.PreferencePayerRequest.builder()
                        .name(estudianteNombre)
                        .email(estudianteEmail)
                        .build())
                .build();

        System.out.println("Enviando request a Mercado Pago...");
        
        try {
            PreferenceClient client = new PreferenceClient();
            System.out.println("Cliente de preferencias creado, enviando solicitud...");
            
            Preference preference = client.create(preferenceRequest);
            System.out.println("✅ Preferencia creada exitosamente: " + preference.getId());
            System.out.println("📋 Init Point: " + preference.getInitPoint());
            System.out.println("🧪 Sandbox Init Point: " + preference.getSandboxInitPoint());
            
            // Imprimir métodos de pago configurados
            if (preference.getPaymentMethods() != null) {
                System.out.println("💳 Métodos de pago configurados:");
                System.out.println("   - Excluded Payment Methods: " + preference.getPaymentMethods().getExcludedPaymentMethods());
                System.out.println("   - Excluded Payment Types: " + preference.getPaymentMethods().getExcludedPaymentTypes());
                System.out.println("   - Installments: " + preference.getPaymentMethods().getInstallments());
            } else {
                System.out.println("⚠️ No hay configuración específica de métodos de pago (se usarán todos los disponibles)");
            }
            
            return preference;
            
        } catch (MPApiException apiEx) {
            System.err.println("\n╔══════════════════════════════════════════════════╗");
            System.err.println("║    ❌ ERROR DE API MERCADO PAGO                 ║");
            System.err.println("╚══════════════════════════════════════════════════╝");
            System.err.println("📊 Código HTTP: " + apiEx.getStatusCode());
            System.err.println("💬 Mensaje: " + apiEx.getMessage());
            
            if (apiEx.getApiResponse() != null) {
                System.err.println("\n┌─── Respuesta de la API ───────────────────────┐");
                System.err.println("│ Status: " + apiEx.getApiResponse().getStatusCode());
                System.err.println("│ Content: " + apiEx.getApiResponse().getContent());
                System.err.println("│ Headers: " + apiEx.getApiResponse().getHeaders());
                System.err.println("└───────────────────────────────────────────────┘");
            } else {
                System.err.println("⚠️  No hay respuesta de la API disponible");
            }
            
            System.err.println("\n════════════════════════════════════════════════════\n");
            // RE-LANZAR LA EXCEPCIÓN ORIGINAL para que el controlador pueda capturarla
            throw apiEx;
            
        } catch (MPException mpEx) {
            System.err.println("\n╔══════════════════════════════════════════════════╗");
            System.err.println("║    ❌ ERROR DE MERCADO PAGO                     ║");
            System.err.println("╚══════════════════════════════════════════════════╝");
            System.err.println("💬 Mensaje: " + mpEx.getMessage());
            System.err.println("🔍 Causa: " + mpEx.getCause());
            System.err.println("\n════════════════════════════════════════════════════\n");
            // RE-LANZAR LA EXCEPCIÓN ORIGINAL
            throw mpEx;
            
        } catch (Exception ex) {
            System.err.println("\n╔══════════════════════════════════════════════════╗");
            System.err.println("║    ❌ ERROR GENERAL                             ║");
            System.err.println("╚══════════════════════════════════════════════════╝");
            System.err.println("📦 Tipo: " + ex.getClass().getName());
            System.err.println("💬 Mensaje: " + ex.getMessage());
            System.err.println("\n🔍 Stack trace:");
            ex.printStackTrace();
            System.err.println("\n════════════════════════════════════════════════════\n");
            throw new RuntimeException("Error inesperado al crear preferencia: " + ex.getMessage(), ex);
        }
    }

    /**
     * Obtener información de un pago específico
     */
    public Payment obtenerPago(Long paymentId) throws MPException, MPApiException {
        PaymentClient client = new PaymentClient();
        return client.get(paymentId);
    }

    /**
     * Verificar si un pago fue aprobado
     */
    public boolean isPagoAprobado(Payment payment) {
        return payment != null && "approved".equals(payment.getStatus());
    }
}
