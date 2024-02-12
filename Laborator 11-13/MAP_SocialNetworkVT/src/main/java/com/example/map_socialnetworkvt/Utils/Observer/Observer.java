package com.example.map_socialnetworkvt.Utils.Observer;

import com.example.map_socialnetworkvt.Utils.Events.Event;

public interface Observer <E extends Event> {
    void update(E e);
}
