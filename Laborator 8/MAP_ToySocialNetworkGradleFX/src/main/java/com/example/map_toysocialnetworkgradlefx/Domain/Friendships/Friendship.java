package com.example.map_toysocialnetworkgradlefx.Domain.Friendships;


import com.example.map_toysocialnetworkgradlefx.Domain.Entity;
import com.example.map_toysocialnetworkgradlefx.Domain.Tuple;
import com.example.map_toysocialnetworkgradlefx.Domain.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Friendship extends Entity<Tuple<Long,Long>> {
    private LocalDateTime date;
    private User user2;
    private User user1;
    private FriendshipStatus status;

    /**
     * Creates Friendship
     * @param user1 first one
     * @param user2 second one
     * @param date the friendship was made
     */
    public Friendship(User user1,User user2,LocalDateTime date,FriendshipStatus status)
    {
        this.user1=user1;
        this.user2=user2;
        this.date=date;
        Long uuid1=user1.getId();
        Long uuid2=user2.getId();
        Tuple<Long,Long> UUIDComb=new Tuple<>(uuid1,uuid2);
        this.setId(UUIDComb);
        this.status=status;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    /**
     * Formmater for time
     * @return string formatter
     */
    private String formatter(LocalDateTime time)
    {
        String time_printed;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        time_printed=time.format(formatter);
        return time_printed;
    }

    /**
     * to string function between users
     */
    @Override
    public String toString() {
        return "{Friendship between: "+user1.getUsername()+" "+user2.getUsername()+" , date: "+formatter(getDate())+"}";
    }

    /**
     *
     * @return date of friendship
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * sets date of friendship
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
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
}
