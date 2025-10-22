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
    },

    // Actualizar Categoría
    updateCategoria: async (id, categoriaData) => {
        try {
            console.log('Actualizando categoría:', id, categoriaData);
            const response = await fetch(`${BASE_URL}/api/categorias/actualizar`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ id, ...categoriaData })
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al actualizar categoría: ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error actualizando categoría:', error);
            throw error;
        }
    },

    // Eliminar Categoría
    deleteCategoria: async (id) => {
        try {
            console.log('Eliminando categoría:', id);
            const response = await fetch(`${BASE_URL}/api/categorias/eliminar/${id}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al eliminar categoría: ${text}`);
            }
            
            console.log('Categoría eliminada exitosamente');
        } catch (error) {
            console.error('Error eliminando categoría:', error);
            throw error;
        }
    },

    // Actualizar Materia
    updateMateria: async (id, materiaData) => {
        try {
            console.log('Actualizando materia:', id, materiaData);
            const response = await fetch(`${BASE_URL}/api/materias/actualizar`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ id, ...materiaData })
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al actualizar materia: ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error actualizando materia:', error);
            throw error;
        }
    },

    // Eliminar Materia
    deleteMateria: async (id) => {
        try {
            console.log('Eliminando materia:', id);
            const response = await fetch(`${BASE_URL}/api/materias/eliminar/${id}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al eliminar materia: ${text}`);
            }
            
            console.log('Materia eliminada exitosamente');
        } catch (error) {
            console.error('Error eliminando materia:', error);
            throw error;
        }
    },

    // Obtener Usuario por ID
    getUser: async (id) => {
        try {
            const usuarios = await api.getUsers();
            return usuarios.find(u => u.id === id);
        } catch (error) {
            console.error('Error obteniendo usuario:', error);
            throw error;
        }
    },

    // Actualizar Usuario
    updateUser: async (id, userData) => {
        try {
            console.log('Actualizando usuario:', id, userData);
            const response = await fetch(`${BASE_URL}/api/usuarios/actualizar`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ id, ...userData })
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al actualizar usuario: ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error actualizando usuario:', error);
            throw error;
        }
    },

    // Eliminar Usuario
    deleteUser: async (id) => {
        try {
            console.log('Eliminando usuario:', id);
            const response = await fetch(`${BASE_URL}/api/usuarios/eliminar/${id}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al eliminar usuario: ${text}`);
            }
            
            console.log('Usuario eliminado exitosamente');
        } catch (error) {
            console.error('Error eliminando usuario:', error);
            throw error;
        }
    },

    // Obtener Materia por ID
    getMateria: async (id) => {
        try {
            console.log('Obteniendo materia:', id);
            const response = await fetch(`${BASE_URL}/api/materias/${id}`);
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al obtener materia: ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error obteniendo materia:', error);
            throw error;
        }
    },

    // Obtener Categoría por ID
    getCategoria: async (id) => {
        try {
            console.log('Obteniendo categoría:', id);
            const response = await fetch(`${BASE_URL}/api/categorias/${id}`);
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al obtener categoría: ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error obteniendo categoría:', error);
            throw error;
        }
    },

    // ========== CONTENIDOS ==========

    // Listar todos los contenidos
    getContenidos: async () => {
        try {
            console.log('Obteniendo contenidos...');
            const response = await fetch(`${BASE_URL}/api/contenidos/listar`);
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error HTTP: ${response.status} - ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error obteniendo contenidos:', error);
            throw error;
        }
    },

    // Listar contenidos por materia
    getContenidosByMateria: async (materiaId) => {
        try {
            console.log('Obteniendo contenidos de materia:', materiaId);
            const response = await fetch(`${BASE_URL}/api/contenidos/materia/${materiaId}`);
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error HTTP: ${response.status} - ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error obteniendo contenidos de materia:', error);
            throw error;
        }
    },

    // Obtener contenido por ID
    getContenido: async (id) => {
        try {
            const response = await fetch(`${BASE_URL}/api/contenidos/${id}`);
            
            if (!response.ok) {
                throw new Error(`Error al obtener contenido: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error obteniendo contenido:', error);
            throw error;
        }
    },

    // Descargar archivo de contenido
    descargarContenido: async (id) => {
        try {
            window.open(`${BASE_URL}/api/contenidos/descargar/${id}`, '_blank');
        } catch (error) {
            console.error('Error descargando contenido:', error);
            throw error;
        }
    },

    // Crear contenido con archivo
    createContenido: async (formData) => {
        try {
            console.log('Creando contenido...');
            const response = await fetch(`${BASE_URL}/api/contenidos/crear`, {
                method: 'POST',
                body: formData // FormData ya incluye el Content-Type correcto
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al crear contenido: ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error creando contenido:', error);
            throw error;
        }
    },

    // Actualizar contenido
    updateContenido: async (id, formData) => {
        try {
            console.log('Actualizando contenido:', id);
            const response = await fetch(`${BASE_URL}/api/contenidos/actualizar/${id}`, {
                method: 'PUT',
                body: formData
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al actualizar contenido: ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error actualizando contenido:', error);
            throw error;
        }
    },

    // Eliminar contenido
    deleteContenido: async (id) => {
        try {
            console.log('Eliminando contenido:', id);
            const response = await fetch(`${BASE_URL}/api/contenidos/eliminar/${id}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al eliminar contenido: ${text}`);
            }
            
            console.log('Contenido eliminado exitosamente');
        } catch (error) {
            console.error('Error eliminando contenido:', error);
            throw error;
        }
    },

    // ==================== FOROS ====================
    
    // Crear foro
    createForo: async (tema, materiaId) => {
        try {
            console.log('Creando foro...', { tema, materiaId });
            const response = await fetch(`${BASE_URL}/api/foros/crear`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ tema, materiaId })
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al crear foro: ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error creando foro:', error);
            throw error;
        }
    },
    
    // Obtener foros por materia
    getForosByMateria: async (materiaId) => {
        try {
            console.log('Obteniendo foros de materia:', materiaId);
            const response = await fetch(`${BASE_URL}/api/foros/materia/${materiaId}`);
            
            if (!response.ok) {
                throw new Error(`Error al obtener foros: ${response.status}`);
            }
            
            const foros = await response.json();
            console.log('Foros obtenidos:', foros.length);
            return foros;
        } catch (error) {
            console.error('Error obteniendo foros:', error);
            throw error;
        }
    },
    
    // Obtener foro específico
    getForo: async (id) => {
        try {
            const response = await fetch(`${BASE_URL}/api/foros/${id}`);
            
            if (!response.ok) {
                throw new Error(`Error al obtener foro: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error obteniendo foro:', error);
            throw error;
        }
    },
    
    // Eliminar foro
    deleteForo: async (id) => {
        try {
            const response = await fetch(`${BASE_URL}/api/foros/${id}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                throw new Error(`Error al eliminar foro: ${response.status}`);
            }
            
            console.log('Foro eliminado exitosamente');
        } catch (error) {
            console.error('Error eliminando foro:', error);
            throw error;
        }
    },
    
    // ==================== MENSAJES DE FORO ====================
    
    // Crear mensaje en foro
    createMensajeForo: async (foroId, usuarioId, contenido) => {
        try {
            console.log('Creando mensaje...', { foroId, usuarioId, contenido });
            const response = await fetch(`${BASE_URL}/api/foros/${foroId}/mensajes`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ usuarioId, contenido })
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al crear mensaje: ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error creando mensaje:', error);
            throw error;
        }
    },
    
    // Obtener mensajes de un foro
    getMensajesForo: async (foroId) => {
        try {
            console.log('Obteniendo mensajes del foro:', foroId);
            const response = await fetch(`${BASE_URL}/api/foros/${foroId}/mensajes`);
            
            if (!response.ok) {
                throw new Error(`Error al obtener mensajes: ${response.status}`);
            }
            
            const mensajes = await response.json();
            console.log('Mensajes obtenidos:', mensajes.length);
            return mensajes;
        } catch (error) {
            console.error('Error obteniendo mensajes:', error);
            throw error;
        }
    },
    
    // Eliminar mensaje
    deleteMensajeForo: async (id) => {
        try {
            const response = await fetch(`${BASE_URL}/api/foros/mensajes/${id}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                throw new Error(`Error al eliminar mensaje: ${response.status}`);
            }
            
            console.log('Mensaje eliminado exitosamente');
        } catch (error) {
            console.error('Error eliminando mensaje:', error);
            throw error;
        }
    },

    // ==================== REPORTES ====================
    
    // Crear reporte de contenido inapropiado
    createReporte: async (usuarioId, contenidoId, motivo) => {
        try {
            console.log('Creando reporte...', { usuarioId, contenidoId, motivo });
            const response = await fetch(`${BASE_URL}/api/reportes/crear`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ usuarioId, contenidoId, motivo })
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al crear reporte: ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error creando reporte:', error);
            throw error;
        }
    },
    
    // Obtener todos los reportes
    getReportes: async () => {
        try {
            const response = await fetch(`${BASE_URL}/api/reportes/listar`);
            
            if (!response.ok) {
                throw new Error(`Error al obtener reportes: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error obteniendo reportes:', error);
            throw error;
        }
    },
    
    // Obtener reportes por estado
    getReportesByEstado: async (estado) => {
        try {
            const response = await fetch(`${BASE_URL}/api/reportes/estado/${estado}`);
            
            if (!response.ok) {
                throw new Error(`Error al obtener reportes: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error obteniendo reportes por estado:', error);
            throw error;
        }
    },
    
    // Obtener reportes de un contenido
    getReportesByContenido: async (contenidoId) => {
        try {
            const response = await fetch(`${BASE_URL}/api/reportes/contenido/${contenidoId}`);
            
            if (!response.ok) {
                throw new Error(`Error al obtener reportes: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error obteniendo reportes del contenido:', error);
            throw error;
        }
    },
    
    // Cambiar estado de reporte
    cambiarEstadoReporte: async (reporteId, nuevoEstado) => {
        try {
            const response = await fetch(`${BASE_URL}/api/reportes/${reporteId}/estado`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ estado: nuevoEstado })
            });
            
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Error al cambiar estado: ${text}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error cambiando estado del reporte:', error);
            throw error;
        }
    },
    
    // Eliminar reporte
    deleteReporte: async (id) => {
        try {
            const response = await fetch(`${BASE_URL}/api/reportes/${id}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                throw new Error(`Error al eliminar reporte: ${response.status}`);
            }
            
            console.log('Reporte eliminado exitosamente');
        } catch (error) {
            console.error('Error eliminando reporte:', error);
            throw error;
        }
    }
};