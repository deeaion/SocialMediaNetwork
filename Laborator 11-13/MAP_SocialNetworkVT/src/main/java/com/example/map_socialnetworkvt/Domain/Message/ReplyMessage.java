package com.example.map_socialnetworkvt.Domain.Message;

import com.example.map_socialnetworkvt.Domain.Entity;
import com.example.map_socialnetworkvt.Domain.Tuple;
import com.example.map_socialnetworkvt.Domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ReplyMessage extends Message {
    private Message toReplyto;

    public ReplyMessage(User userSender, String message, LocalDateTime data, List<User> users
    ,Message toReplyto) {
        super(userSender, message, data, users);
        this.toReplyto=toReplyto;
    }


    @Override
    public String toString() {
        return "ReplyMessage{" +
                "toReplyto=" + toReplyto.getUserSender().getUsername() +
                ", reciever=" + this.getUserSender().getUsername() +
                ", dataReciever=" + this.getData() +
                ", reply='" + this.getMessage()+ '\'' +
                '}';
    }

    public Message getReplyfor() {
        return toReplyto;
    }

    public void setReplyfor(Message toReplyto) {
        this.toReplyto = toReplyto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReplyMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getReplyfor(), that.getReplyfor()) && Objects.equals(this.getUserSender(), that.getUserSender()) && Objects.equals(getData(), that.getData()) && Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getReplyfor(),
                this.getUserSender(), this.getData(), this.getReplyfor());
    }

}
