package com.example.map_toysocialnetworkgradlefx.Utils.Observer;

import com.example.map_toysocialnetworkgradlefx.Utils.Events.Event;

public interface Observer <E extends Event> {
    void update(E e);
}
