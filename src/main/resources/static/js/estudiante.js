// Variables globales
let categoriaSeleccionada = null;
let materiaSeleccionada = null;
let tieneSuscripcionActiva = false;

// Verificar autenticación al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    verificarAuth();
    verificarEstadoSuscripcion();
    cargarCategorias();
});

// Verificar autenticación
function verificarAuth() {
    const userRole = (localStorage.getItem('userRole') || '').toString().trim();
    const userName = localStorage.getItem('userName');
    
    // Comparación case-insensitive
    if (!userRole || userRole.toLowerCase() !== 'estudiante') {
        alert('Acceso denegado. Debe iniciar sesión como estudiante.');
        window.location.href = '/index.html';
        return;
    }
    
    // Mostrar nombre del estudiante
    const userNameElement = document.getElementById('nombreEstudiante');
    if (userNameElement) {
        userNameElement.textContent = userName || 'Estudiante';
    }
}

// Verificar estado de suscripción
async function verificarEstadoSuscripcion() {
    const userId = localStorage.getItem('userId');
    if (!userId) return;
    
    try {
        const response = await api.verificarSuscripcion(userId);
        tieneSuscripcionActiva = response.tieneSuscripcion;
        
        const estadoSuscripcion = document.getElementById('estado-suscripcion');
        const btnSuscribirse = document.getElementById('btn-suscribirse');
        
        if (tieneSuscripcionActiva) {
            estadoSuscripcion.style.display = 'inline-block';
            btnSuscribirse.style.display = 'none';
        } else {
            estadoSuscripcion.style.display = 'none';
            btnSuscribirse.style.display = 'inline-block';
        }
    } catch (error) {
        console.error('Error verificando suscripción:', error);
    }
}

// Mostrar modal de suscripción
function mostrarModalSuscripcion() {
    const modal = document.getElementById('modal-suscripcion');
    modal.style.display = 'flex';
}

// Cerrar modal de suscripción
function cerrarModalSuscripcion() {
    const modal = document.getElementById('modal-suscripcion');
    modal.style.display = 'none';
}

// Procesar suscripción con Mercado Pago
async function procesarSuscripcion() {
    const userId = localStorage.getItem('userId');
    if (!userId) {
        alert('Error: No se pudo identificar al usuario');
        return;
    }
    
    const btnProcesar = document.getElementById('btn-procesar-pago');
    const loader = document.getElementById('loader-pago');
    
    try {
        btnProcesar.disabled = true;
        btnProcesar.textContent = 'Procesando...';
        loader.style.display = 'block';
        
        // Crear preferencia de pago
        const response = await api.crearPreferenciaPago(userId);
        
        if (response.suscripcionActiva) {
            alert('Ya tienes una suscripción activa');
            cerrarModalSuscripcion();
            return;
        }
        
        // Redirigir a Mercado Pago (usar sandboxInitPoint para pruebas)
        window.location.href = response.sandboxInitPoint;
        
    } catch (error) {
        console.error('Error procesando pago:', error);
        alert('Error al procesar el pago: ' + error.message);
        btnProcesar.disabled = false;
        btnProcesar.textContent = 'Proceder al Pago';
        loader.style.display = 'none';
    }
}

// Cargar todas las categorías
async function cargarCategorias() {
    try {
        const categorias = await api.getCategorias();
        mostrarCategoriasEnCards(categorias);
    } catch (error) {
        console.error('Error al cargar categorías:', error);
        alert('Error al cargar las categorías');
    }
}

// Mostrar categorías como tarjetas
function mostrarCategoriasEnCards(categorias) {
    const container = document.getElementById('categorias-container');
    container.innerHTML = '';
    
    if (categorias.length === 0) {
        container.innerHTML = '<p class="no-data">No hay categorías disponibles</p>';
        return;
    }
    
    categorias.forEach(categoria => {
        const card = document.createElement('div');
        card.className = 'categoria-card';
        card.innerHTML = `
            <h3>${categoria.nombre}</h3>
            <button onclick="verMateriasPorCategoria(${categoria.id}, '${categoria.nombre}')">
                Ver Materias
            </button>
        `;
        container.appendChild(card);
    });
}

// Ver materias de una categoría
async function verMateriasPorCategoria(categoriaId, categoriaNombre) {
    try {
        categoriaSeleccionada = { id: categoriaId, nombre: categoriaNombre };
        
        const materias = await api.getMaterias();
        const materiasFiltradas = materias.filter(m => m.categoria.id === categoriaId);
        
        // Actualizar título de la sección
        document.getElementById('categoria-titulo').textContent = `Materias de ${categoriaNombre}`;
        
        // Mostrar materias
        mostrarMateriasEnCards(materiasFiltradas);
        
        // Ocultar categorías y mostrar materias
        document.getElementById('categorias-section').style.display = 'none';
        document.getElementById('materias-section').style.display = 'block';
        document.getElementById('contenidos-section').style.display = 'none';
    } catch (error) {
        console.error('Error al cargar materias:', error);
        alert('Error al cargar las materias de la categoría');
    }
}

// Mostrar materias como tarjetas
function mostrarMateriasEnCards(materias) {
    const container = document.getElementById('materias-container');
    container.innerHTML = '';
    
    if (materias.length === 0) {
        container.innerHTML = '<p class="no-data">No hay materias disponibles en esta categoría</p>';
        return;
    }
    
    materias.forEach(materia => {
        const card = document.createElement('div');
        card.className = 'materia-card';
        card.innerHTML = `
            <h3>${materia.nombre}</h3>
            <p class="materia-categoria">${materia.categoria.nombre}</p>
            <button onclick="verContenidosDeMateria(${materia.id}, '${materia.nombre}')">
                Ver Contenidos
            </button>
        `;
        container.appendChild(card);
    });
}

// Ver contenidos de una materia
async function verContenidosDeMateria(materiaId, materiaNombre) {
    try {
        materiaSeleccionada = { id: materiaId, nombre: materiaNombre };
        
        const contenidos = await api.getContenidosByMateria(materiaId);
        
        // Actualizar título de la sección
        document.getElementById('materia-titulo').textContent = `Contenidos de ${materiaNombre}`;
        
        // Mostrar contenidos
        mostrarContenidosEnTabla(contenidos);
        
        // Ocultar materias y mostrar contenidos
        document.getElementById('materias-section').style.display = 'none';
        document.getElementById('contenidos-section').style.display = 'block';
    } catch (error) {
        console.error('Error al cargar contenidos:', error);
        alert('Error al cargar los contenidos de la materia');
    }
}

// Mostrar contenidos en tabla
function mostrarContenidosEnTabla(contenidos) {
    const tbody = document.getElementById('contenidos-tabla');
    const noContenidos = document.getElementById('no-contenidos');
    
    tbody.innerHTML = '';
    
    if (contenidos.length === 0) {
        noContenidos.style.display = 'block';
        return;
    }
    
    noContenidos.style.display = 'none';
    
    contenidos.forEach(contenido => {
        const row = document.createElement('tr');
        
        row.innerHTML = `
            <td>${contenido.titulo}</td>
            <td>${contenido.tipo}</td>
            <td>${contenido.descripcion || 'Sin descripción'}</td>
            <td>
                <button class="btn-descargar" onclick="descargarContenido(${contenido.id}, '${contenido.archivo}')">
                    Descargar
                </button>
                <button class="btn-reportar" onclick="abrirModalReporte(${contenido.id}, '${contenido.titulo.replace(/'/g, "\\'")}')">
                    ⚠️ Reportar
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Descargar contenido
async function descargarContenido(contenidoId, nombreArchivo) {
    try {
        // Primero obtener información del contenido
        const contenido = await api.getContenido(contenidoId);
        
        // Si es un examen, verificar suscripción
        if (contenido.tipo === 'Examen') {
            if (!tieneSuscripcionActiva) {
                if (confirm('Para descargar exámenes necesitas una suscripción activa. ¿Deseas suscribirte ahora?')) {
                    mostrarModalSuscripcion();
                }
                return;
            }
        }
        
        // Si tiene suscripción o no es un examen, proceder con la descarga
        const userId = localStorage.getItem('userId');
        console.log(`Descargando contenido ${contenidoId}: ${nombreArchivo}`);
        window.open(`http://localhost:8080/api/contenidos/descargar/${contenidoId}?estudianteId=${userId}`, '_blank');
        console.log('Descarga iniciada correctamente');
    } catch (error) {
        console.error('Error al descargar contenido:', error);
        if (error.message.includes('suscripción')) {
            if (confirm('Para descargar exámenes necesitas una suscripción activa. ¿Deseas suscribirte ahora?')) {
                mostrarModalSuscripcion();
            }
        } else {
            alert('Error al descargar el archivo');
        }
    }
}

// Mostrar/ocultar formulario de subida
function toggleFormularioContenido() {
    const formulario = document.getElementById('formulario-contenido');
    const boton = document.getElementById('btn-mostrar-formulario');
    
    if (formulario.style.display === 'none') {
        formulario.style.display = 'block';
        boton.textContent = '− Ocultar Formulario';
    } else {
        formulario.style.display = 'none';
        boton.textContent = '+ Subir Nuevo Contenido';
        limpiarFormularioContenido();
    }
}

// Cancelar subida de contenido
function cancelarSubida() {
    toggleFormularioContenido();
}

// Limpiar formulario
function limpiarFormularioContenido() {
    document.getElementById('titulo-contenido').value = '';
    document.getElementById('descripcion-contenido').value = '';
    document.getElementById('tipo-contenido').value = '';
    document.getElementById('archivo-contenido').value = '';
}

// Subir contenido
async function subirContenido(event) {
    event.preventDefault();
    
    if (!materiaSeleccionada) {
        alert('Error: No se ha seleccionado una materia');
        return;
    }
    
    const titulo = document.getElementById('titulo-contenido').value;
    const descripcion = document.getElementById('descripcion-contenido').value;
    const tipo = document.getElementById('tipo-contenido').value;
    const archivo = document.getElementById('archivo-contenido').files[0];
    
    if (!archivo) {
        alert('Por favor seleccione un archivo');
        return;
    }
    
    // Validar tamaño del archivo (50MB)
    const maxSize = 50 * 1024 * 1024; // 50MB en bytes
    if (archivo.size > maxSize) {
        alert('El archivo es demasiado grande. El tamaño máximo es 50MB');
        return;
    }
    
    try {
        // Crear FormData
        const formData = new FormData();
        formData.append('titulo', titulo);
        formData.append('descripcion', descripcion);
        formData.append('tipo', tipo);
        formData.append('materiaId', materiaSeleccionada.id);
        formData.append('archivo', archivo);
        
        console.log('Subiendo contenido...', {
            titulo,
            descripcion,
            tipo,
            materiaId: materiaSeleccionada.id,
            archivo: archivo.name
        });
        
        // Mostrar mensaje de carga
        const botonSubmit = event.target.querySelector('button[type="submit"]');
        const textoOriginal = botonSubmit.textContent;
        botonSubmit.textContent = 'Subiendo...';
        botonSubmit.disabled = true;
        
        // Enviar al backend
        await api.createContenido(formData);
        
        alert('Contenido subido exitosamente');
        
        // Limpiar formulario y ocultarlo
        limpiarFormularioContenido();
        toggleFormularioContenido();
        
        // Recargar la lista de contenidos
        await verContenidosDeMateria(materiaSeleccionada.id, materiaSeleccionada.nombre);
        
        // Restaurar botón
        botonSubmit.textContent = textoOriginal;
        botonSubmit.disabled = false;
        
    } catch (error) {
        console.error('Error al subir contenido:', error);
        alert('Error al subir el contenido. Por favor intente nuevamente.');
        
        // Restaurar botón en caso de error
        const botonSubmit = event.target.querySelector('button[type="submit"]');
        if (botonSubmit) {
            botonSubmit.textContent = 'Subir Contenido';
            botonSubmit.disabled = false;
        }
    }
}

// Volver a la vista de categorías
function mostrarCategorias() {
    document.getElementById('categorias-section').style.display = 'block';
    document.getElementById('materias-section').style.display = 'none';
    document.getElementById('contenidos-section').style.display = 'none';
    
    // Ocultar formulario si está visible
    const formulario = document.getElementById('formulario-contenido');
    if (formulario && formulario.style.display !== 'none') {
        toggleFormularioContenido();
    }
    
    categoriaSeleccionada = null;
    materiaSeleccionada = null;
    
    cargarCategorias();
}

// Volver a la vista de materias
function volverAMaterias() {
    if (categoriaSeleccionada) {
        document.getElementById('materias-section').style.display = 'block';
        document.getElementById('contenidos-section').style.display = 'none';
        
        materiaSeleccionada = null;
        
        // Recargar materias de la categoría seleccionada
        verMateriasPorCategoria(categoriaSeleccionada.id, categoriaSeleccionada.nombre);
    }
}

// Cerrar sesión
function logout() {
    if (confirm('¿Está seguro que desea cerrar sesión?')) {
        localStorage.clear();
        window.location.href = '/index.html';
    }
}

// ==================== FUNCIONES DE FOROS ====================

let foroActual = null;

// Alternar entre pestañas de Contenidos y Foros
function mostrarTab(tab) {
    const tabContenidos = document.getElementById('tab-contenidos');
    const tabForos = document.getElementById('tab-foros');
    const contentContenidos = document.getElementById('tab-contenidos-content');
    const contentForos = document.getElementById('tab-foros-content');
    
    if (tab === 'contenidos') {
        tabContenidos.classList.add('active');
        tabForos.classList.remove('active');
        contentContenidos.style.display = 'block';
        contentForos.style.display = 'none';
    } else if (tab === 'foros') {
        tabContenidos.classList.remove('active');
        tabForos.classList.add('active');
        contentContenidos.style.display = 'none';
        contentForos.style.display = 'block';
        
        // Cargar foros al mostrar la pestaña
        cargarForos();
    }
}

// Cargar foros de la materia actual
async function cargarForos() {
    if (!materiaSeleccionada) {
        return;
    }
    
    try {
        const foros = await api.getForosByMateria(materiaSeleccionada.id);
        mostrarListaForos(foros);
    } catch (error) {
        console.error('Error al cargar foros:', error);
        alert('Error al cargar los foros');
    }
}

// Mostrar lista de foros
function mostrarListaForos(foros) {
    const container = document.getElementById('foros-lista');
    const noForos = document.getElementById('no-foros');
    
    // Ocultar vista de mensajes
    document.getElementById('foro-mensajes-section').style.display = 'none';
    container.style.display = 'block';
    
    if (foros.length === 0) {
        noForos.style.display = 'block';
        // Limpiar foros anteriores
        const foroCards = container.querySelectorAll('.foro-card');
        foroCards.forEach(card => card.remove());
        return;
    }
    
    noForos.style.display = 'none';
    
    // Limpiar foros anteriores
    const foroCards = container.querySelectorAll('.foro-card');
    foroCards.forEach(card => card.remove());
    
    // Crear tarjeta para cada foro
    foros.forEach(foro => {
        const card = document.createElement('div');
        card.className = 'foro-card';
        card.innerHTML = `
            <h4>${foro.tema}</h4>
            <div class="foro-info">
                <span>📊 Mensajes: ${foro.mensajesForo ? foro.mensajesForo.length : 0}</span>
            </div>
            <button onclick="verMensajesForo(${foro.id}, '${foro.tema.replace(/'/g, "\\'")}')">
                Ver Discusión
            </button>
        `;
        container.appendChild(card);
    });
}

// Ver mensajes de un foro
async function verMensajesForo(foroId, tema) {
    foroActual = { id: foroId, tema: tema };
    
    try {
        const mensajes = await api.getMensajesForo(foroId);
        
        // Actualizar título
        document.getElementById('foro-tema-actual').textContent = tema;
        
        // Mostrar mensajes
        mostrarMensajes(mensajes);
        
        // Ocultar lista de foros y mostrar vista de mensajes
        document.getElementById('foros-lista').style.display = 'none';
        document.getElementById('foro-mensajes-section').style.display = 'block';
    } catch (error) {
        console.error('Error al cargar mensajes:', error);
        alert('Error al cargar los mensajes del foro');
    }
}

// Mostrar mensajes en el foro
function mostrarMensajes(mensajes) {
    const container = document.getElementById('mensajes-container');
    container.innerHTML = '';
    
    if (mensajes.length === 0) {
        container.innerHTML = '<p class="no-data">No hay mensajes aún. ¡Sé el primero en participar!</p>';
        return;
    }
    
    mensajes.forEach(mensaje => {
        const mensajeDiv = document.createElement('div');
        mensajeDiv.className = 'mensaje';
        
        const fecha = new Date(mensaje.fecha).toLocaleString('es-ES', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
        
        const esPropio = mensaje.usuario.id == localStorage.getItem('userId');
        
        mensajeDiv.innerHTML = `
            <div class="mensaje-header">
                <strong>${mensaje.usuario.nombre}</strong>
                <span class="mensaje-fecha">${fecha}</span>
            </div>
            <div class="mensaje-contenido">${mensaje.contenido}</div>
            ${esPropio ? `<button class="btn-delete-mensaje" onclick="eliminarMensaje(${mensaje.id})">Eliminar</button>` : ''}
        `;
        
        container.appendChild(mensajeDiv);
    });
    
    // Scroll al final
    container.scrollTop = container.scrollHeight;
}

// Enviar mensaje al foro
async function enviarMensaje(event) {
    event.preventDefault();
    
    if (!foroActual) {
        alert('Error: No se ha seleccionado un foro');
        return;
    }
    
    const contenido = document.getElementById('contenido-mensaje').value.trim();
    
    if (!contenido) {
        alert('Por favor escribe un mensaje');
        return;
    }
    
    const usuarioId = localStorage.getItem('userId');
    
    try {
        const botonSubmit = event.target.querySelector('button[type="submit"]');
        botonSubmit.textContent = 'Enviando...';
        botonSubmit.disabled = true;
        
        await api.createMensajeForo(foroActual.id, usuarioId, contenido);
        
        // Limpiar textarea
        document.getElementById('contenido-mensaje').value = '';
        
        // Recargar mensajes
        const mensajes = await api.getMensajesForo(foroActual.id);
        mostrarMensajes(mensajes);
        
        botonSubmit.textContent = 'Enviar Mensaje';
        botonSubmit.disabled = false;
    } catch (error) {
        console.error('Error al enviar mensaje:', error);
        alert('Error al enviar el mensaje');
        
        const botonSubmit = event.target.querySelector('button[type="submit"]');
        if (botonSubmit) {
            botonSubmit.textContent = 'Enviar Mensaje';
            botonSubmit.disabled = false;
        }
    }
}

// Eliminar mensaje
async function eliminarMensaje(mensajeId) {
    if (!confirm('¿Está seguro de eliminar este mensaje?')) {
        return;
    }
    
    try {
        await api.deleteMensajeForo(mensajeId);
        
        // Recargar mensajes
        const mensajes = await api.getMensajesForo(foroActual.id);
        mostrarMensajes(mensajes);
    } catch (error) {
        console.error('Error al eliminar mensaje:', error);
        alert('Error al eliminar el mensaje');
    }
}

// Volver a la lista de foros
function volverAListaForos() {
    foroActual = null;
    cargarForos();
}

// Mostrar/ocultar formulario de crear foro
function toggleFormularioForo() {
    const formulario = document.getElementById('formulario-foro');
    const boton = document.getElementById('btn-crear-foro');
    
    if (formulario.style.display === 'none') {
        formulario.style.display = 'block';
        boton.textContent = '− Ocultar Formulario';
    } else {
        formulario.style.display = 'none';
        boton.textContent = '+ Crear Nuevo Foro';
        document.getElementById('tema-foro').value = '';
    }
}

// Cancelar creación de foro
function cancelarForo() {
    toggleFormularioForo();
}

// Crear foro
async function crearForo(event) {
    event.preventDefault();
    
    if (!materiaSeleccionada) {
        alert('Error: No se ha seleccionado una materia');
        return;
    }
    
    const tema = document.getElementById('tema-foro').value.trim();
    
    if (!tema) {
        alert('Por favor ingrese un tema para el foro');
        return;
    }
    
    try {
        const botonSubmit = event.target.querySelector('button[type="submit"]');
        botonSubmit.textContent = 'Creando...';
        botonSubmit.disabled = true;
        
        await api.createForo(tema, materiaSeleccionada.id);
        
        alert('Foro creado exitosamente');
        
        // Limpiar y ocultar formulario
        document.getElementById('tema-foro').value = '';
        toggleFormularioForo();
        
        // Recargar lista de foros
        await cargarForos();

        botonSubmit.textContent = 'Crear Foro';
        botonSubmit.disabled = false;
    } catch (error) {
        console.error('Error al crear foro:', error);
        alert('Error al crear el foro');

        const botonSubmit = event.target.querySelector('button[type="submit"]');
        if (botonSubmit) {
            botonSubmit.textContent = 'Crear Foro';
            botonSubmit.disabled = false;
        }
    }
}

// ==================== FUNCIONES DE REPORTES ====================

// Abrir modal para reportar contenido
function abrirModalReporte(contenidoId, tituloContenido) {
    const modal = document.getElementById('modal-reportar');
    document.getElementById('contenido-reportado-id').value = contenidoId;
    
    // Limpiar formulario
    document.getElementById('motivo-select').value = '';
    document.getElementById('descripcion-reporte').value = '';
    
    // Mostrar modal
    modal.style.display = 'flex';
}

// Cerrar modal de reporte
function cerrarModalReporte() {
    const modal = document.getElementById('modal-reportar');
    modal.style.display = 'none';
    
    // Limpiar formulario
    document.getElementById('motivo-select').value = '';
    document.getElementById('descripcion-reporte').value = '';
    document.getElementById('contenido-reportado-id').value = '';
}

// Enviar reporte
async function enviarReporte(event) {
    event.preventDefault();
    
    const contenidoId = document.getElementById('contenido-reportado-id').value;
    const motivoSeleccionado = document.getElementById('motivo-select').value;
    const descripcionAdicional = document.getElementById('descripcion-reporte').value.trim();
    
    // Construir motivo completo
    let motivoCompleto = motivoSeleccionado;
    if (descripcionAdicional) {
        motivoCompleto += `: ${descripcionAdicional}`;
    }
    
    const usuarioId = localStorage.getItem('userId');
    
    if (!usuarioId) {
        alert('Error: No se pudo identificar al usuario');
        return;
    }
    
    try {
        const botonSubmit = event.target.querySelector('button[type="submit"]');
        botonSubmit.textContent = 'Enviando Reporte...';
        botonSubmit.disabled = true;
        
        await api.createReporte(usuarioId, contenidoId, motivoCompleto);
        
        alert('✅ Reporte enviado exitosamente. Un administrador lo revisará pronto.');
        
        // Cerrar modal
        cerrarModalReporte();
        
        botonSubmit.textContent = 'Enviar Reporte';
        botonSubmit.disabled = false;
    } catch (error) {
        console.error('Error al enviar reporte:', error);
        alert('❌ Error al enviar el reporte. Por favor intente nuevamente.');
        
        const botonSubmit = event.target.querySelector('button[type="submit"]');
        if (botonSubmit) {
            botonSubmit.textContent = 'Enviar Reporte';
            botonSubmit.disabled = false;
        }
    }
}
