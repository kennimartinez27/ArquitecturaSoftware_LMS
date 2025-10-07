const BASE_URL = 'http://localhost:8080';

// Objeto para manejar todas las llamadas a la API
const api = {
    // Autenticación
    login: async (username, password) => {
        try {
            console.log('Intentando login con:', { username });
            const response = await fetch(`${BASE_URL}/api/usuarios/listar`);
            
            if (!response.ok) {
                console.error('Error en la respuesta:', response.status, response.statusText);
                throw new Error(`Error en la autenticación: ${response.status}`);
            }
            
            const usuarios = await response.json();
            console.log('Usuarios recuperados:', usuarios.length);
            
            const usuario = usuarios.find(u => u.correo === username && u.contrasena === password);
            console.log('Usuario encontrado:', usuario ? 'Sí' : 'No');
            
            if (!usuario) {
                throw new Error('Credenciales inválidas');
            }
            return usuario;
        } catch (error) {
            console.error('Error en login:', error);
            throw error;
        }
    },

    // Usuarios
    getUsers: async () => {
        try {
            console.log('Obteniendo usuarios...');
            const response = await fetch(`${BASE_URL}/api/usuarios/listar`);
            console.log('Respuesta usuarios:', response.status, response.statusText);
            if (!response.ok) {
                const text = await response.text();
                console.error('Error response:', text);
                throw new Error(`Error HTTP: ${response.status} - ${text}`);
            }
            const data = await response.json();
            console.log('Datos de usuarios:', data);
            return data;
        } catch (error) {
            console.error('Error completo obteniendo usuarios:', error);
            throw error;
        }
    },

    // Materias
    getMaterias: async () => {
        try {
            console.log('Obteniendo materias...');
            const response = await fetch(`${BASE_URL}/api/materias/listar`);
            console.log('Respuesta materias:', response.status, response.statusText);
            if (!response.ok) {
                const text = await response.text();
                console.error('Error response:', text);
                throw new Error(`Error HTTP: ${response.status} - ${text}`);
            }
            const data = await response.json();
            console.log('Datos de materias:', data);
            return data;
        } catch (error) {
            console.error('Error completo obteniendo materias:', error);
            throw error;
        }
    },

    // Categorías
    getCategorias: async () => {
        try {
            console.log('Obteniendo categorías...');
            const response = await fetch(`${BASE_URL}/api/categorias/listar`);
            console.log('Respuesta categorías:', response.status, response.statusText);
            if (!response.ok) {
                const text = await response.text();
                console.error('Error response:', text);
                throw new Error(`Error HTTP: ${response.status} - ${text}`);
            }
            const data = await response.json();
            console.log('Datos de categorías:', data);
            return data;
        } catch (error) {
            console.error('Error completo obteniendo categorías:', error);
            throw error;
        }
    },

    // Crear Categoría
    createCategoria: async (categoriaData) => {
        try {
            console.log('Creando categoría:', categoriaData);
            const response = await fetch(`${BASE_URL}/api/categorias/crear`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(categoriaData)
            });
            
            if (!response.ok) {
                const text = await response.text();
                console.error('Error response:', text);
                throw new Error(`Error al crear categoría: ${text}`);
            }
            
            const data = await response.json();
            console.log('Categoría creada:', data);
            return data;
        } catch (error) {
            console.error('Error completo creando categoría:', error);
            throw error;
        }
    },

    // Crear Materia
    createMateria: async (materiaData) => {
        try {
            console.log('Creando materia:', materiaData);
            const response = await fetch(`${BASE_URL}/api/materias/crear`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(materiaData)
            });
            
            if (!response.ok) {
                const text = await response.text();
                console.error('Error response:', text);
                throw new Error(`Error al crear materia: ${text}`);
            }
            
            const data = await response.json();
            console.log('Materia creada:', data);
            return data;
        } catch (error) {
            console.error('Error completo creando materia:', error);
            throw error;
        }
    },

    // Crear Usuario
    createUser: async (userData) => {
        try {
            console.log('Creando usuario:', userData);
            const response = await fetch(`${BASE_URL}/api/usuarios/crear`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });
            
            if (!response.ok) {
                const text = await response.text();
                console.error('Error response:', text);
                throw new Error(`Error al crear usuario: ${text}`);
            }
            
            const data = await response.json();
            console.log('Usuario creado:', data);
            return data;
        } catch (error) {
            console.error('Error completo creando usuario:', error);
            throw error;
        }
    }
};