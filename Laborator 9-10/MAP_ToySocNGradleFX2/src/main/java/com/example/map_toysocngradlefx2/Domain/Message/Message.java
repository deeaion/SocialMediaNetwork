package com.example.map_toysocngradlefx2.Domain.Message;

import com.example.map_toysocngradlefx2.Domain.Entity;
import com.example.map_toysocngradlefx2.Domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {
    private User userSender;
    private String message;
    private LocalDateTime data;
    private List<User> users;

    public Message(User userSender, String message, LocalDateTime data, List<User> users) {
        this.userSender = userSender;
        //this.userReciver = userReciver;
        this.message = message;
        this.data = data;
        this.users = users;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message1)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getUserSender(), message1.getUserSender()) && Objects.equals(getMessage(), message1.getMessage()) && Objects.equals(getData(), message1.getData()) && Objects.equals(getUsers(), message1.getUsers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUserSender(), getMessage(), getData(), getUsers());
    }

    @Override
    public String toString() {
        String toS= "Message{" +
                "userSender=" + userSender +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", users=" ;
        for (User u : users) {
            toS += u.getUsername()+" ";
        }
        toS+="}";
    return toS;}
}
