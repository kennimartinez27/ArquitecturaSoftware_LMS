-- Script SQL para activar manualmente una suscripción de prueba
-- Ejecuta esto en pgAdmin o DBeaver para simular un pago exitoso

-- Ver suscripciones actuales
SELECT * FROM suscripcion WHERE estudiante_id = 4;

-- Activar la suscripción pendiente más reciente del estudiante ID 4
UPDATE suscripcion 
SET 
    estado_pago = 'approved',
    estado_suscripcion = 'activa',
    payment_id = 'TEST-MANUAL-' || CAST(EXTRACT(EPOCH FROM NOW()) AS VARCHAR),
    metodo_pago = 'credit_card',
    detalles_adicionales = 'Pago activado manualmente para pruebas'
WHERE estudiante_id = 4 
  AND estado_suscripcion = 'inactiva'
  AND estado_pago = 'pending'
  AND id = (
    SELECT id FROM suscripcion 
    WHERE estudiante_id = 4 
      AND estado_suscripcion = 'inactiva'
    ORDER BY fecha_pago DESC 
    LIMIT 1
  );

-- Verificar que se activó
SELECT 
    id,
    estudiante_id,
    estado_pago,
    estado_suscripcion,
    monto_pagado,
    fecha_pago,
    payment_id
FROM suscripcion 
WHERE estudiante_id = 4
ORDER BY fecha_pago DESC;
