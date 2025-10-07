// Verificar autenticación
function verificarAuth() {
    const userRole = localStorage.getItem('userRole');
    const userName = localStorage.getItem('userName');
    
    if (!userName || userRole !== 'Administrador') {
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
                        <button onclick="editarUsuario(${usuario.id})">Editar</button>
                        <button onclick="eliminarUsuario(${usuario.id})">Eliminar</button>
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
                        <button onclick="editarMateria(${materia.id})">Editar</button>
                        <button onclick="eliminarMateria(${materia.id})">Eliminar</button>
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
                        <button onclick="editarCategoria(${categoria.id})">Editar</button>
                        <button onclick="eliminarCategoria(${categoria.id})">Eliminar</button>
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

// Event Listeners para los formularios
document.addEventListener('DOMContentLoaded', () => {
    verificarAuth();
    cargarUsuarios();
    cargarMaterias();
    cargarCategorias();

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