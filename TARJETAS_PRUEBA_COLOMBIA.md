# 💳 Tarjetas de Prueba para Mercado Pago Colombia

## 🇨🇴 Tarjetas Oficiales de Prueba MCO (Colombia)

### ✅ Mastercard - APROBADO
```
Número: 5474925432670366
Vencimiento: 11/25
CVV: 123
Nombre completo: APRO
Tipo de documento: C.C (Cédula de Ciudadanía)
Número de documento: 12345678
```

### ✅ Visa - APROBADO  
```
Número: 4075595716483764
Vencimiento: 11/25
CVV: 123
Nombre completo: APRO
Tipo de documento: C.C
Número de documento: 12345678
```

### ❌ Mastercard - RECHAZADO
```
Número: 5031433215406351
Nombre: OTHE
```

### ⏳ Visa - PENDIENTE
```
Número: 4509953566233704
Nombre: CONT
```

---

## 🔧 Si las tarjetas no funcionan:

### Opción 1: Verificar modo TEST
1. Ve a: https://www.mercadopago.com.co/developers/panel/credentials
2. Asegúrate que el toggle "Modo TEST" esté **ACTIVADO** (verde)
3. Si está en producción, las tarjetas de prueba NO funcionarán

### Opción 2: Verificar cuenta
En algunos casos, Mercado Pago Colombia requiere:
- ✅ Email verificado
- ✅ Teléfono verificado
- ✅ Documento de identidad verificado

### Opción 3: Probar con PSE
Si aparece PSE como método de pago alternativo en la página de checkout:
- En modo TEST, puedes seleccionar cualquier banco
- Usuario de prueba: `APRO`
- Contraseña: `test`

---

## 📱 Datos adicionales que puede pedir el formulario:

```
Email: test@test.com
Teléfono: 3001234567
Ciudad: Bogotá
Dirección: Calle 123 #45-67
Código Postal: 110111
```

---

## 🔍 Verificar que estás en modo TEST

Ejecuta este comando en PowerShell para confirmar:

```powershell
Invoke-RestMethod -Uri "https://api.mercadopago.com/users/me" -Headers @{"Authorization"="Bearer TU_TOKEN_AQUI"}
```

Debe mostrar:
- `country_id: CO`
- `site_id: MCO`

---

## 🚀 Documentación oficial:

https://www.mercadopago.com.co/developers/es/docs/checkout-api/testing
