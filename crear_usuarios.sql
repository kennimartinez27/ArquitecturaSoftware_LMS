
INSERT INTO usuario (nombre, correo, contrasena, rol) VALUES
('Juan Pérez Admin', 'admin@educa.com', 'admin123', 'Administrador'),
('María González Admin', 'maria.admin@educa.com', 'admin456', 'Administrador');

-- Insertar en la tabla Administrador (referenciando los IDs de Usuario)
INSERT INTO administrador (id) 
SELECT id FROM usuario WHERE rol = 'Administrador';

-- ============================================
-- INSERTAR PROFESORES
-- ============================================

-- Insertar en la tabla Usuario
INSERT INTO usuario (nombre, correo, contrasena, rol) VALUES
('Carlos Ramírez', 'carlos.ramirez@educa.com', 'profesor123', 'Profesor'),
('Ana Martínez', 'ana.martinez@educa.com', 'profesor456', 'Profesor'),
('Luis Hernández', 'luis.hernandez@educa.com', 'profesor789', 'Profesor'),
('Patricia López', 'patricia.lopez@educa.com', 'profesor101', 'Profesor'),
('Roberto Sánchez', 'roberto.sanchez@educa.com', 'profesor202', 'Profesor');

-- Insertar en la tabla Profesor
INSERT INTO profesor (id) 
SELECT id FROM usuario WHERE rol = 'Profesor';

-- ============================================
-- INSERTAR ESTUDIANTES
-- ============================================

-- Insertar en la tabla Usuario
INSERT INTO usuario (nombre, correo, contrasena, rol) VALUES
('Pedro García', 'pedro.garcia@estudiante.com', 'est123', 'Estudiante'),
('Laura Torres', 'laura.torres@estudiante.com', 'est456', 'Estudiante'),
('Miguel Ángel Ruiz', 'miguel.ruiz@estudiante.com', 'est789', 'Estudiante'),
('Carmen Díaz', 'carmen.diaz@estudiante.com', 'est101', 'Estudiante'),
('José Manuel Castro', 'jose.castro@estudiante.com', 'est202', 'Estudiante'),
('Sofia Morales', 'sofia.morales@estudiante.com', 'est303', 'Estudiante'),
('Diego Vargas', 'diego.vargas@estudiante.com', 'est404', 'Estudiante'),
('Valentina Rojas', 'valentina.rojas@estudiante.com', 'est505', 'Estudiante'),
('Andrés Mendoza', 'andres.mendoza@estudiante.com', 'est606', 'Estudiante'),
('Isabella Flores', 'isabella.flores@estudiante.com', 'est707', 'Estudiante'),
('Santiago Jiménez', 'santiago.jimenez@estudiante.com', 'est808', 'Estudiante'),
('Camila Ortiz', 'camila.ortiz@estudiante.com', 'est909', 'Estudiante'),
('Mateo Silva', 'mateo.silva@estudiante.com', 'est111', 'Estudiante'),
('Lucía Romero', 'lucia.romero@estudiante.com', 'est222', 'Estudiante'),
('Daniel Navarro', 'daniel.navarro@estudiante.com', 'est333', 'Estudiante');

-- Insertar en la tabla Estudiante
INSERT INTO estudiante (id) 
SELECT id FROM usuario WHERE rol = 'Estudiante';

-- ============================================
-- VERIFICACIÓN
-- ============================================

-- Contar usuarios por rol
SELECT 
    rol, 
    COUNT(*) as total
FROM usuario
GROUP BY rol
ORDER BY rol;

-- Ver todos los usuarios creados
SELECT 
    u.id,
    u.nombre,
    u.correo,
    u.rol,
    CASE 
        WHEN a.id IS NOT NULL THEN 'Tabla Administrador'
        WHEN p.id IS NOT NULL THEN 'Tabla Profesor'
        WHEN e.id IS NOT NULL THEN 'Tabla Estudiante'
        ELSE 'Sin tabla específica'
    END as tabla_especifica
FROM usuario u
LEFT JOIN administrador a ON u.id = a.id
LEFT JOIN profesor p ON u.id = p.id
LEFT JOIN estudiante e ON u.id = e.id
ORDER BY u.id;
