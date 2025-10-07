document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const response = await api.login(username, password);
            if (response) {
                // Guardar los datos del usuario
                localStorage.setItem('userId', response.id);
                localStorage.setItem('userEmail', response.correo);
                localStorage.setItem('userName', response.nombre);
                localStorage.setItem('userRole', response.rol);

                // Redirigir según el tipo de usuario
                switch(response.rol) {
                    case 'Administrador':
                        window.location.href = '/admin/dashboard.html';
                        break;
                    case 'Profesor':
                        window.location.href = '/profesor/dashboard.html';
                        break;
                    case 'Estudiante':
                        window.location.href = '/estudiante/dashboard.html';
                        break;
                    default:
                        alert('Tipo de usuario no reconocido');
                }
            } else {
                alert('Credenciales inválidas');
            }
        } catch (error) {
            console.error('Error en el login:', error);
            alert('Error al iniciar sesión');
        }
    });
});