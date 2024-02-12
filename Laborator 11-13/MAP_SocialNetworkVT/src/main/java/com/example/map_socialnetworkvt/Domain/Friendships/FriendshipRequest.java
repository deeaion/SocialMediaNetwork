package com.example.map_socialnetworkvt.Domain.Friendships;

import com.example.map_socialnetworkvt.Domain.Entity;
import com.example.map_socialnetworkvt.Domain.Tuple;
import com.example.map_socialnetworkvt.Domain.User;

import java.util.Objects;

public class FriendshipRequest extends Entity<Tuple<Long,Long>>
{
    /**
     * Creates Friendship
     *
     * @param user1  first one
     * @param user2  second one
     * @param date   the friendship was made
     * @param status
     */
    private User user2;
    private User user1;
    FriendshipStatus status;
    public FriendshipRequest(User user1, User user2, FriendshipStatus status) {
        this.user1=user1;
        this.user2=user2;
        Long uuid1=user1.getId();
        Long uuid2=user2.getId();
        Tuple<Long,Long> UUIDComb=new Tuple<>(uuid1,uuid2);
        this.setId(UUIDComb);
        this.status=status;
    }

    @Override
    public String toString() {
        return "FriendshipRequest{" +
                "user2=" + user2 +
                ", user1=" + user1 +
                ", status=" + status.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendshipRequest request)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getUser2(), request.getUser2()) && Objects.equals(getUser1(), request.getUser1()) && getStatus() == request.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUser2(), getUser1(), getStatus());
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }
}
