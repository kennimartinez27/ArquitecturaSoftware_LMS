package com.lms.educa.Entidades;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Suscripcion")
public class Suscripcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;
    
    @Column(nullable = false)
    private LocalDateTime fechaPago;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPagado;
    
    @Column(nullable = false, length = 50)
    private String estadoPago; // pending, approved, rejected, cancelled
    
    @Column(length = 100)
    private String preferenciaId; // ID de la preferencia de Mercado Pago
    
    @Column(length = 100)
    private String paymentId; // ID del pago completado
    
    @Column(nullable = false, length = 20)
    private String estadoSuscripcion; // activa, inactiva
    
    @Column(length = 50)
    private String metodoPago; // credit_card, debit_card, etc.
    
    @Column(columnDefinition = "TEXT")
    private String detallesAdicionales;
    
    public Suscripcion() {
        this.fechaPago = LocalDateTime.now();
        this.estadoPago = "pending";
        this.estadoSuscripcion = "inactiva";
    }
}
