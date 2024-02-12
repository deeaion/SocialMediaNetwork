package com.example.map_toysocialnetworkgradlefx.Domain.Message;

import com.example.map_toysocialnetworkgradlefx.Domain.Entity;
import com.example.map_toysocialnetworkgradlefx.Domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {
    private User userSender;
    private User userReciver;
    private String message;
    private LocalDateTime data;
    private List<User> users;

    public Message(User userSender, User userReciver, String message, LocalDateTime data, List<User> users) {
        this.userSender = userSender;
        this.userReciver = userReciver;
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

    public User getUserReciver() {
        return userReciver;
    }

    public void setUserReciver(User userReciver) {
        this.userReciver = userReciver;
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
        return Objects.equals(getUserSender(), message1.getUserSender()) && Objects.equals(getUserReciver(), message1.getUserReciver()) && Objects.equals(getMessage(), message1.getMessage()) && Objects.equals(getData(), message1.getData()) && Objects.equals(getUsers(), message1.getUsers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUserSender(), getUserReciver(), getMessage(), getData(), getUsers());
    }

    @Override
    public String toString() {
        return "Message{" +
                "userSender=" + userSender +
                ", userReciver=" + userReciver +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", users=" + users +
                '}';
    }
}
