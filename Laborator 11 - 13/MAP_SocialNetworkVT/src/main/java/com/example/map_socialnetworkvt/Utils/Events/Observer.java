package com.example.map_socialnetworkvt.Utils.Events;

public interface Observer <E extends Event> {
    void update(E e);
}
