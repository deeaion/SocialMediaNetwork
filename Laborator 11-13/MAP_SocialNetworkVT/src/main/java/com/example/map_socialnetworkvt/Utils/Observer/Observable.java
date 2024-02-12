package com.example.map_socialnetworkvt.Utils.Observer;

import com.example.map_socialnetworkvt.Utils.Events.Event;

public interface Observable <E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
