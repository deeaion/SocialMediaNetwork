package com.example.map_socialnetworkvt.Utils.Events;

import com.example.map_socialnetworkvt.Domain.Entity;

public class SocialNetworkChangeEvent<E extends Entity> implements Event {
    private ChangeEventType type;
    private E data,oldData;

    public SocialNetworkChangeEvent(ChangeEventType type, E data)
    {
        this.type=type;
        this.data=data;
    }
    public SocialNetworkChangeEvent(ChangeEventType type,E data,E oldData)
    {
        this.type=type;
        this.data=data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public E getData() {
        return data;
    }

    public E getOldData() {
        return oldData;
    }
}
