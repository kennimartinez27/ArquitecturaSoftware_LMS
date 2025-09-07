package com.lms.educa.interfaces;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String evento, Object data) {
        for (Observer observer : observers) {
            observer.update(evento, data);
        }
    }
}
