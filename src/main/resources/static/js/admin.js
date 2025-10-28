// Verificar autenticación
function verificarAuth() {
    const userRole = (localStorage.getItem('userRole') || '').toString().trim();
    const userName = localStorage.getItem('userName');
    
    // Comparación case-insensitive
    if (!userName || userRole.toLowerCase() !== 'administrador') {
        window.location.href = '/index.html';
        return;
    }
    console.log('Usuario autenticado:', userName, 'Rol:', userRole);
}

// Función de logout
function logout() {
    localStorage.removeItem('userId');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userName');
    localStorage.removeItem('userRole');
    window.location.href = '/index.html';
}

// ==================== NAVEGACIÓN DEL MENÚ ====================
function mostrarSeccion(seccion) {
    // Ocultar todas las secciones
    const secciones = document.querySelectorAll('section');
    secciones.forEach(s => s.classList.remove('active'));
    
    // Desactivar todos los botones del menú
    const menuItems = document.querySelectorAll('.menu-item');
    menuItems.forEach(item => item.classList.remove('active'));
    
    // Mostrar la sección seleccionada
    const seccionActiva = document.getElementById(`${seccion}-section`);
    if (seccionActiva) {
        seccionActiva.classList.add('active');
    }
    
    // Activar el botón del menú correspondiente
    event.target.closest('.menu-item').classList.add('active');
}

// Funciones para mostrar/ocultar formularios
function mostrarFormularioUsuario() {
    document.getElementById('formulario-usuario').style.display = 'block';
}

function mostrarFormularioMateria() {
    document.getElementById('formulario-materia').style.display = 'block';
}

function mostrarFormularioCategoria() {
    document.getElementById('formulario-categoria').style.display = 'block';
}

// Funciones para cargar datos
async function cargarUsuarios() {
    try {
        const usuarios = await api.getUsers();
        const tabla = document.getElementById('usuarios-tabla');
        tabla.innerHTML = '';
        
        usuarios.forEach(usuario => {
            tabla.innerHTML += `
                <tr>
                    <td>${usuario.correo}</td>
                    <td>${usuario.rol}</td>
                    <td>${usuario.nombre}</td>
                    <td>${usuario.correo}</td>
                    <td>
                        <button class="btn-edit" onclick="editarUsuario(${usuario.id})">Editar</button>
                        <button class="btn-delete" onclick="eliminarUsuario(${usuario.id})">Eliminar</button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        console.error('Error cargando usuarios:', error);
        alert('Error al cargar usuarios');
    }
}

async function cargarMaterias() {
    try {
        const materias = await api.getMaterias();
        const tabla = document.getElementById('materias-tabla');
        tabla.innerHTML = '';
        
        materias.forEach(materia => {
            tabla.innerHTML += `
                <tr>
                    <td>${materia.nombre}</td>
                    <td>${materia.categoria ? materia.categoria.nombre : 'Sin categoría'}</td>
                    <td>
                        <button class="btn-edit" onclick="editarMateria(${materia.id})">Editar</button>
                        <button class="btn-delete" onclick="eliminarMateria(${materia.id})">Eliminar</button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        console.error('Error cargando materias:', error);
        alert('Error al cargar materias');
    }
}

async function cargarCategorias() {
    try {
        const categorias = await api.getCategorias();
        const tabla = document.getElementById('categorias-tabla');
        tabla.innerHTML = '';
        
        categorias.forEach(categoria => {
            tabla.innerHTML += `
                <tr>
                    <td>${categoria.nombre}</td>
                    <td>
                        <button class="btn-edit" onclick="editarCategoria(${categoria.id})">Editar</button>
                        <button class="btn-delete" onclick="eliminarCategoria(${categoria.id})">Eliminar</button>
                    </td>
                </tr>
            `;
        });

        // También actualizar el select de categorías en el formulario de materias
        const select = document.getElementById('categoriaMateria');
        select.innerHTML = '<option value="">Seleccione categoría</option>';
        categorias.forEach(categoria => {
            select.innerHTML += `<option value="${categoria.id}">${categoria.nombre}</option>`;
        });
    } catch (error) {
        console.error('Error cargando categorías:', error);
        alert('Error al cargar categorías');
    }
}

// Funciones para eliminar
async function eliminarUsuario(id) {
    if (!confirm('¿Está seguro de que desea eliminar este usuario?')) {
        return;
    }
    
    try {
        await api.deleteUser(id);
        alert('Usuario eliminado exitosamente');
        cargarUsuarios();
    } catch (error) {
        console.error('Error eliminando usuario:', error);
        alert('Error al eliminar usuario: ' + error.message);
    }
}

async function eliminarMateria(id) {
    try {
        const materia = await api.getMateria(id);
        if (!materia) {
            alert('Materia no encontrada');
            return;
        }
        
        const mensaje = `¿Está seguro de que desea eliminar la materia "${materia.nombre}"?\n\n` +
                       `ADVERTENCIA: Esta acción eliminará también todo el contenido asociado ` +
                       `(contenidos, foros, evaluaciones, etc.)`;
        
        if (!confirm(mensaje)) {
            return;
        }
        
        await api.deleteMateria(id);
        alert('Materia eliminada exitosamente');
        cargarMaterias();
    } catch (error) {
        console.error('Error eliminando materia:', error);
        alert('Error al eliminar materia: ' + error.message);
    }
}

async function eliminarCategoria(id) {
    try {
        // Primero obtener la categoría para verificar si tiene materias
        const categoria = await api.getCategoria(id);
        if (!categoria) {
            alert('Categoría no encontrada');
            return;
        }
        
        // Obtener todas las materias para contar cuántas tienen esta categoría
        const materias = await api.getMaterias();
        const materiasConCategoria = materias.filter(m => m.categoria && m.categoria.id === id);
        
        let mensaje = '¿Está seguro de que desea eliminar esta categoría?';
        if (materiasConCategoria.length > 0) {
            mensaje = `ADVERTENCIA: Esta categoría tiene ${materiasConCategoria.length} materia(s) asociada(s).\n\n` +
                      `No se puede eliminar una categoría con materias asociadas.\n` +
                      `Por favor, reasigne o elimine las materias primero.\n\n` +
                      `Materias: ${materiasConCategoria.map(m => m.nombre).join(', ')}`;
            alert(mensaje);
            return;
        }
        
        if (!confirm(mensaje)) {
            return;
        }
        
        await api.deleteCategoria(id);
        alert('Categoría eliminada exitosamente');
        cargarCategorias();
        cargarMaterias(); // Recargar materias por si alguna tenía esta categoría
    } catch (error) {
        console.error('Error eliminando categoría:', error);
        alert('Error al eliminar categoría: ' + error.message);
    }
}

// Funciones para editar
async function editarUsuario(id) {
    try {
        const usuario = await api.getUser(id);
        if (!usuario) {
            alert('Usuario no encontrado');
            return;
        }
        
        const nuevoNombre = prompt('Nombre:', usuario.nombre);
        if (nuevoNombre === null) return; // Cancelado
        
        const nuevoCorreo = prompt('Correo:', usuario.correo);
        if (nuevoCorreo === null) return;
        
        if (!nuevoNombre.trim() || !nuevoCorreo.trim()) {
            alert('Los campos no pueden estar vacíos');
            return;
        }
        
        const userData = {
            nombre: nuevoNombre,
            correo: nuevoCorreo,
            contrasena: usuario.contrasena,
            rol: usuario.rol
        };
        
        await api.updateUser(id, userData);
        alert('Usuario actualizado exitosamente');
        cargarUsuarios();
    } catch (error) {
        console.error('Error editando usuario:', error);
        alert('Error al editar usuario: ' + error.message);
    }
}

async function editarMateria(id) {
    try {
        const materia = await api.getMateria(id);
        if (!materia) {
            alert('Materia no encontrada');
            return;
        }
        
        const nuevoNombre = prompt('Nombre de la materia:', materia.nombre);
        if (nuevoNombre === null) return; // Cancelado
        
        // Obtener categorías para mostrar opciones
        const categorias = await api.getCategorias();
        let mensaje = 'Seleccione el ID de la categoría:\n';
        categorias.forEach(cat => {
            mensaje += `${cat.id}: ${cat.nombre}\n`;
        });
        
        const nuevaCategoriaId = prompt(mensaje, materia.categoria ? materia.categoria.id : '');
        if (nuevaCategoriaId === null) return;
        
        const materiaData = {
            nombre: nuevoNombre,
            categoria: {
                id: parseInt(nuevaCategoriaId)
            }
        };
        
        await api.updateMateria(id, materiaData);
        alert('Materia actualizada exitosamente');
        cargarMaterias();
    } catch (error) {
        console.error('Error editando materia:', error);
        alert('Error al editar materia: ' + error.message);
    }
}

async function editarCategoria(id) {
    try {
        const categoria = await api.getCategoria(id);
        if (!categoria) {
            alert('Categoría no encontrada');
            return;
        }
        
        const nuevoNombre = prompt('Nombre de la categoría:', categoria.nombre);
        if (nuevoNombre === null) return; // Cancelado
        
        if (!nuevoNombre.trim()) {
            alert('El nombre no puede estar vacío');
            return;
        }
        
        await api.updateCategoria(id, { nombre: nuevoNombre });
        alert('Categoría actualizada exitosamente');
        cargarCategorias();
        cargarMaterias(); // Recargar materias para mostrar el nombre actualizado de la categoría
    } catch (error) {
        console.error('Error editando categoría:', error);
        alert('Error al editar categoría: ' + error.message);
    }
}

// Event Listeners para los formularios
document.addEventListener('DOMContentLoaded', () => {
    verificarAuth();
    
    // Mostrar la sección de usuarios por defecto
    document.getElementById('usuarios-section').classList.add('active');
    
    cargarUsuarios();
    cargarMaterias();
    cargarCategorias();
    cargarReportes(); // Cargar reportes al inicio
    cargarSuscripciones(); // Cargar suscripciones al inicio
    cargarEstadisticasSuscripciones(); // Cargar estadísticas

    // Formulario de Usuario
    document.getElementById('userForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const userData = {
            tipo: document.getElementById('userType').value,
            nombre: document.getElementById('nombre').value,
            correo: document.getElementById('correo').value,
            contrasena: document.getElementById('contrasena').value
        };

        try {
            await api.createUser(userData);
            alert('Usuario creado exitosamente');
            document.getElementById('userForm').reset();
            document.getElementById('formulario-usuario').style.display = 'none';
            cargarUsuarios();
        } catch (error) {
            console.error('Error creando usuario:', error);
            alert('Error al crear usuario');
        }
    });

    // Formulario de Materia
    document.getElementById('materiaForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const materiaData = {
            nombre: document.getElementById('nombreMateria').value,
            categoria: {
                id: parseInt(document.getElementById('categoriaMateria').value)
            }
        };

        try {
            await api.createMateria(materiaData);
            alert('Materia creada exitosamente');
            document.getElementById('materiaForm').reset();
            document.getElementById('formulario-materia').style.display = 'none';
            cargarMaterias();
        } catch (error) {
            console.error('Error creando materia:', error);
            alert('Error al crear materia');
        }
    });

    // Formulario de Categoría
    document.getElementById('categoriaForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const categoriaData = {
            nombre: document.getElementById('nombreCategoria').value
        };

        try {
            await api.createCategoria(categoriaData);
            alert('Categoría creada exitosamente');
            document.getElementById('categoriaForm').reset();
            document.getElementById('formulario-categoria').style.display = 'none';
            cargarCategorias();
        } catch (error) {
            console.error('Error creando categoría:', error);
            alert('Error al crear categoría');
        }
    });
});

// ==================== FUNCIONES DE SUSCRIPCIONES ====================

let todasLasSuscripciones = [];

// Cargar estadísticas de suscripciones
async function cargarEstadisticasSuscripciones() {
    try {
        const stats = await api.getEstadisticasSuscripciones();
        const suscripciones = await api.getSuscripciones();
        
        document.getElementById('stat-activas').textContent = stats.suscripcionesActivas || 0;
        document.getElementById('stat-totales').textContent = suscripciones.length || 0;
        document.getElementById('stat-precio').textContent = `$${stats.precioSuscripcion || 0}`;
    } catch (error) {
        console.error('Error cargando estadísticas:', error);
    }
}

// Cargar todas las suscripciones
async function cargarSuscripciones() {
    try {
        const suscripciones = await api.getSuscripciones();
        todasLasSuscripciones = suscripciones;
        mostrarSuscripciones(suscripciones);
    } catch (error) {
        console.error('Error cargando suscripciones:', error);
        alert('Error al cargar suscripciones');
    }
}

// Mostrar suscripciones en la tabla
function mostrarSuscripciones(suscripciones) {
    const tabla = document.getElementById('suscripciones-tabla');
    tabla.innerHTML = '';
    
    if (suscripciones.length === 0) {
        tabla.innerHTML = '<tr><td colspan="8" class="no-data">No hay suscripciones para mostrar</td></tr>';
        return;
    }
    
    suscripciones.forEach(sus => {
        const fecha = new Date(sus.fechaPago).toLocaleString('es-ES', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
        
        const estadoPagoBadge = getBadgeEstadoPago(sus.estadoPago);
        const estadoSuscripcionBadge = getBadgeEstadoSuscripcion(sus.estadoSuscripcion);
        
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${sus.id}</td>
            <td>${sus.estudiante.nombre}</td>
            <td>${sus.estudiante.correo}</td>
            <td>${fecha}</td>
            <td>$${sus.montoPagado}</td>
            <td>${estadoPagoBadge}</td>
            <td>${estadoSuscripcionBadge}</td>
            <td>${sus.metodoPago || 'N/A'}</td>
        `;
        
        tabla.appendChild(row);
    });
}

// Obtener badge para estado de pago
function getBadgeEstadoPago(estado) {
    const badges = {
        'approved': '<span class="badge badge-success">✅ Aprobado</span>',
        'pending': '<span class="badge badge-warning">⏳ Pendiente</span>',
        'rejected': '<span class="badge badge-danger">❌ Rechazado</span>',
        'cancelled': '<span class="badge badge-secondary">🚫 Cancelado</span>'
    };
    return badges[estado] || `<span class="badge">${estado}</span>`;
}

// Obtener badge para estado de suscripción
function getBadgeEstadoSuscripcion(estado) {
    if (estado === 'activa') {
        return '<span class="badge badge-success">✅ Activa</span>';
    } else {
        return '<span class="badge badge-secondary">❌ Inactiva</span>';
    }
}

// Filtrar suscripciones
function filtrarSuscripciones(filtro) {
    // Actualizar botones activos
    const botones = document.querySelectorAll('.suscripciones-filtros .filtro-btn');
    botones.forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    
    let suscripcionesFiltradas = todasLasSuscripciones;
    
    if (filtro === 'activa') {
        suscripcionesFiltradas = todasLasSuscripciones.filter(s => s.estadoSuscripcion === 'activa');
    } else if (filtro === 'inactiva') {
        suscripcionesFiltradas = todasLasSuscripciones.filter(s => s.estadoSuscripcion === 'inactiva');
    } else if (filtro === 'pending') {
        suscripcionesFiltradas = todasLasSuscripciones.filter(s => s.estadoPago === 'pending');
    } else if (filtro === 'approved') {
        suscripcionesFiltradas = todasLasSuscripciones.filter(s => s.estadoPago === 'approved');
    }
    
    mostrarSuscripciones(suscripcionesFiltradas);
}
