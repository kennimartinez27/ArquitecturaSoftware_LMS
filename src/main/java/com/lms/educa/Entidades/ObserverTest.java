package com.lms.educa.Entidades;

public class ObserverTest {
    public static void main(String[] args) {
        // Crear sujetos
        Materia materia = new Materia();
        Contenido contenido = new Contenido();
        Foro foro = new Foro();

        // Crear observadores
        Estudiante estudiante = new Estudiante(null, null, null, null);
        Profesor profesor = new Profesor(null, null, null, null);
        Administrador administrador = new Administrador(null, null, null, null);

        // Registrar observadores en Materia
        materia.addObserver(estudiante);
        materia.addObserver(profesor);
        materia.addObserver(administrador);

        // Registrar observadores en Contenido
        contenido.addObserver(estudiante);
        contenido.addObserver(profesor);

        // Registrar observadores en Foro
        foro.addObserver(estudiante);
        foro.addObserver(administrador);

        // Disparar eventos
        materia.notifyObservers("Nuevo contenido en materia", "Ejemplo de contenido");
        contenido.notifyObservers("Cambio de estado de contenido", "Contenido actualizado");
        foro.notifyObservers("Nuevo mensaje en foro", "Mensaje de prueba");
    }
}
