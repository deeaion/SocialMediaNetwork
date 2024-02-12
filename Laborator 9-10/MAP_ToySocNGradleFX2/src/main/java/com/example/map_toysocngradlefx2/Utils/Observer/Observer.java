package com.example.map_toysocngradlefx2.Utils.Observer;

import com.example.map_toysocngradlefx2.Utils.Events.Event;

public interface Observer <E extends Event> {
    void update(E e);
}
