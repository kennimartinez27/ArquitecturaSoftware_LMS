-- Datos de prueba para el sistema LMS

-- Insertar usuarios de prueba (si no existen)
INSERT INTO Usuario (nombre, correo, contraseña, rol) VALUES 
('Admin', 'admin@email.com', '1234', 'Administrador'),
('Kenny Estudiante', 'kenni@email.com', '1234', 'Estudiante'),
('Profesor Test', 'profesor@email.com', '1234', 'Profesor')
ON CONFLICT (correo) DO NOTHING;

-- Insertar categorías
INSERT INTO Categoria (nombre) VALUES 
('Programación'),
('Matemáticas'),
('Ciencias'),
('Idiomas')
ON CONFLICT DO NOTHING;

-- Insertar materias (ajustar categoria_id según tus datos)
INSERT INTO Materia (nombre, categoria_id) VALUES 
('Java Avanzado', (SELECT id FROM Categoria WHERE nombre = 'Programación' LIMIT 1)),
('Python Básico', (SELECT id FROM Categoria WHERE nombre = 'Programación' LIMIT 1)),
('Algoritmos y Estructuras de Datos', (SELECT id FROM Categoria WHERE nombre = 'Programación' LIMIT 1)),
('Cálculo I', (SELECT id FROM Categoria WHERE nombre = 'Matemáticas' LIMIT 1)),
('Álgebra Lineal', (SELECT id FROM Categoria WHERE nombre = 'Matemáticas' LIMIT 1)),
('Física I', (SELECT id FROM Categoria WHERE nombre = 'Ciencias' LIMIT 1)),
('Química General', (SELECT id FROM Categoria WHERE nombre = 'Ciencias' LIMIT 1)),
('Inglés Intermedio', (SELECT id FROM Categoria WHERE nombre = 'Idiomas' LIMIT 1))
ON CONFLICT DO NOTHING;

-- Insertar algunos contenidos de ejemplo
INSERT INTO Contenido (titulo, descripcion, archivo, estado, materia_id, tipo) VALUES 
('Introducción a POO', 'Conceptos básicos de Programación Orientada a Objetos', 'introduccion_poo.pdf', 'Activo', (SELECT id FROM Materia WHERE nombre = 'Java Avanzado' LIMIT 1), 'Apunte'),
('Ejercicios de Herencia', 'Problemas prácticos sobre herencia en Java', 'ejercicios_herencia.pdf', 'Activo', (SELECT id FROM Materia WHERE nombre = 'Java Avanzado' LIMIT 1), 'Guía'),
('Examen Parcial 1', 'Primera evaluación parcial del curso', 'examen_parcial1.pdf', 'Activo', (SELECT id FROM Materia WHERE nombre = 'Java Avanzado' LIMIT 1), 'Examen'),
('Sintaxis de Python', 'Guía completa de sintaxis básica', 'sintaxis_python.pdf', 'Activo', (SELECT id FROM Materia WHERE nombre = 'Python Básico' LIMIT 1), 'Apunte'),
('Límites y Continuidad', 'Teoría y ejercicios de límites', 'limites.pdf', 'Activo', (SELECT id FROM Materia WHERE nombre = 'Cálculo I' LIMIT 1), 'Apunte')
ON CONFLICT DO NOTHING;

-- Verificar los datos insertados
SELECT 'Usuarios insertados:' AS Info;
SELECT id, nombre, correo, rol FROM Usuario;

SELECT 'Categorías insertadas:' AS Info;
SELECT id, nombre FROM Categoria;

SELECT 'Materias insertadas:' AS Info;
SELECT m.id, m.nombre, c.nombre AS categoria FROM Materia m JOIN Categoria c ON m.categoria_id = c.id;

SELECT 'Contenidos insertados:' AS Info;
SELECT c.id, c.titulo, m.nombre AS materia, c.tipo FROM Contenido c JOIN Materia m ON c.materia_id = m.id;
