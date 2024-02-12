package com.example.map_toysocialnetworkgradlefx.Utils.Events;

import com.example.map_toysocialnetworkgradlefx.Domain.Friendships.Friendship;

public class FriendshipStatusEvent implements Event{
    private FriendshipStatusType type;
    private Friendship friendship;
    public FriendshipStatusEvent(FriendshipStatusType type,Friendship friendship)
    {
        this.type=type;
        this.friendship=friendship;
    }

    public FriendshipStatusType getType() {
        return type;
    }

    public void setType(FriendshipStatusType type) {
        this.type = type;
    }

    public Friendship getFriendship() {
        return friendship;
    }

    public void setFriendship(Friendship friendship) {
        this.friendship = friendship;
    }
}
