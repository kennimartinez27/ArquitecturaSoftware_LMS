// ==================== GESTIÓN DE REPORTES ====================

let reportesCache = [];
let reporteActual = null;
let filtroActual = 'todos';

// Cargar todos los reportes
async function cargarReportes() {
    try {
        const reportes = await api.getReportes();
        reportesCache = reportes;
        actualizarContadores();
        filtrarReportes(filtroActual);
    } catch (error) {
        console.error('Error cargando reportes:', error);
        alert('Error al cargar reportes');
    }
}

// Actualizar contadores de badges
function actualizarContadores() {
    const pendientes = reportesCache.filter(r => r.estado === 'Pendiente').length;
    const revision = reportesCache.filter(r => r.estado === 'En Revisión').length;
    const resueltos = reportesCache.filter(r => r.estado === 'Resuelto').length;
    const rechazados = reportesCache.filter(r => r.estado === 'Rechazado').length;
    const todos = reportesCache.length;

    document.getElementById('count-todos').textContent = todos;
    document.getElementById('count-pendientes').textContent = pendientes;
    document.getElementById('count-revision').textContent = revision;
    document.getElementById('count-resueltos').textContent = resueltos;
    document.getElementById('count-rechazados').textContent = rechazados;
}

// Filtrar reportes por estado
function filtrarReportes(estado) {
    filtroActual = estado;
    
    // Actualizar botones activos
    document.querySelectorAll('.filtro-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    if (event && event.target) {
        event.target.classList.add('active');
    }

    // Filtrar reportes
    let reportesFiltrados = reportesCache;
    if (estado !== 'todos') {
        reportesFiltrados = reportesCache.filter(r => r.estado === estado);
    }

    mostrarReportes(reportesFiltrados);
}

// Mostrar reportes en el contenedor
function mostrarReportes(reportes) {
    const container = document.getElementById('reportes-container');
    const noReportes = document.getElementById('no-reportes');

    if (reportes.length === 0) {
        if (noReportes) {
            noReportes.style.display = 'block';
        }
        container.innerHTML = '<p class="no-data" id="no-reportes">No hay reportes para mostrar</p>';
        return;
    }

    if (noReportes) {
        noReportes.style.display = 'none';
    }
    container.innerHTML = '';

    reportes.forEach(reporte => {
        const card = document.createElement('div');
        card.className = 'reporte-card';
        card.onclick = () => mostrarDetalleReporte(reporte.id);

        const estadoBadge = getEstadoBadgeClass(reporte.estado);
        
        card.innerHTML = `
            <div class="reporte-header">
                <span class="reporte-id">Reporte #${reporte.id}</span>
                <span class="badge ${estadoBadge}">${reporte.estado}</span>
            </div>
            <div class="reporte-info">
                <p><strong>Reportado por:</strong> ${reporte.usuario.nombre} (${reporte.usuario.correo})</p>
                <p><strong>Contenido:</strong> ${reporte.contenido.titulo}</p>
                <p><strong>Motivo:</strong> ${reporte.motivo}</p>
            </div>
        `;

        container.appendChild(card);
    });
}

// Obtener clase CSS para el badge según el estado
function getEstadoBadgeClass(estado) {
    switch (estado) {
        case 'Pendiente':
            return 'badge-warning';
        case 'En Revisión':
            return 'badge-info';
        case 'Resuelto':
            return 'badge-success';
        case 'Rechazado':
            return 'badge-danger';
        default:
            return '';
    }
}

// Mostrar detalles del reporte en modal
async function mostrarDetalleReporte(reporteId) {
    try {
        const reporte = reportesCache.find(r => r.id === reporteId);
        if (!reporte) {
            alert('Reporte no encontrado');
            return;
        }

        reporteActual = reporte;

        // Llenar los datos del modal
        document.getElementById('detalle-id').textContent = reporte.id;
        document.getElementById('detalle-estado').textContent = reporte.estado;
        document.getElementById('detalle-estado').className = `badge ${getEstadoBadgeClass(reporte.estado)}`;
        document.getElementById('detalle-usuario').textContent = `${reporte.usuario.nombre} (${reporte.usuario.correo})`;
        document.getElementById('detalle-motivo').textContent = reporte.motivo;

        // Información del contenido
        document.getElementById('detalle-contenido-titulo').textContent = reporte.contenido.titulo;
        document.getElementById('detalle-contenido-tipo').textContent = reporte.contenido.tipo || 'No especificado';
        document.getElementById('detalle-contenido-descripcion').textContent = reporte.contenido.descripcion || 'Sin descripción';
        document.getElementById('detalle-contenido-materia').textContent = reporte.contenido.materia?.nombre || 'Sin materia';

        // Seleccionar el estado actual
        document.getElementById('select-estado').value = reporte.estado;

        // Mostrar modal
        document.getElementById('modal-detalle-reporte').style.display = 'flex';
    } catch (error) {
        console.error('Error mostrando detalles del reporte:', error);
        alert('Error al mostrar detalles del reporte');
    }
}

// Cerrar modal de detalles
function cerrarModalDetalleReporte() {
    document.getElementById('modal-detalle-reporte').style.display = 'none';
    reporteActual = null;
}

// Cambiar estado del reporte
async function cambiarEstadoReporte() {
    if (!reporteActual) {
        alert('No hay reporte seleccionado');
        return;
    }

    const nuevoEstado = document.getElementById('select-estado').value;
    
    if (nuevoEstado === reporteActual.estado) {
        alert('El estado seleccionado es el mismo que el actual');
        return;
    }

    if (!confirm(`¿Está seguro de cambiar el estado a "${nuevoEstado}"?`)) {
        return;
    }

    try {
        await api.cambiarEstadoReporte(reporteActual.id, nuevoEstado);
        alert('Estado actualizado exitosamente');
        cerrarModalDetalleReporte();
        cargarReportes();
    } catch (error) {
        console.error('Error cambiando estado:', error);
        alert('Error al cambiar estado del reporte');
    }
}

// Descargar contenido reportado
async function descargarContenidoReportado() {
    if (!reporteActual) {
        alert('No hay reporte seleccionado');
        return;
    }

    const contenido = reporteActual.contenido;
    if (!contenido.archivoUrl) {
        alert('Este contenido no tiene archivo para descargar');
        return;
    }

    try {
        // Crear un enlace temporal para descargar
        const link = document.createElement('a');
        link.href = `http://localhost:8080/${contenido.archivoUrl}`;
        link.download = contenido.titulo;
        link.target = '_blank';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    } catch (error) {
        console.error('Error descargando contenido:', error);
        alert('Error al descargar contenido');
    }
}

// Eliminar contenido reportado
async function eliminarContenidoReportado() {
    if (!reporteActual) {
        alert('No hay reporte seleccionado');
        return;
    }

    const mensaje = `¿Está seguro de que desea eliminar el contenido "${reporteActual.contenido.titulo}"?\n\n` +
                   `ADVERTENCIA: Esta acción es irreversible y eliminará el contenido permanentemente.`;
    
    if (!confirm(mensaje)) {
        return;
    }

    try {
        await api.deleteContenido(reporteActual.contenido.id);
        alert('Contenido eliminado exitosamente');
        
        // Cambiar el estado del reporte a "Resuelto" automáticamente
        await api.cambiarEstadoReporte(reporteActual.id, 'Resuelto');
        
        cerrarModalDetalleReporte();
        cargarReportes();
    } catch (error) {
        console.error('Error eliminando contenido:', error);
        alert('Error al eliminar contenido: ' + error.message);
    }
}

// Eliminar reporte actual
async function eliminarReporteActual() {
    if (!reporteActual) {
        alert('No hay reporte seleccionado');
        return;
    }

    const mensaje = `¿Está seguro de que desea eliminar el reporte #${reporteActual.id}?\n\n` +
                   `NOTA: Esto no eliminará el contenido reportado, solo el reporte mismo.`;
    
    if (!confirm(mensaje)) {
        return;
    }

    try {
        await api.deleteReporte(reporteActual.id);
        alert('Reporte eliminado exitosamente');
        cerrarModalDetalleReporte();
        cargarReportes();
    } catch (error) {
        console.error('Error eliminando reporte:', error);
        alert('Error al eliminar reporte: ' + error.message);
    }
}
