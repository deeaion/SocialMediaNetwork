package com.example.map_toysocialnetworkgradlefx.Domain.Message;

import com.example.map_toysocialnetworkgradlefx.Domain.User;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message{
    private Message replyfor;

    public Message getReplyfor() {
        return replyfor;
    }

    public void setReplyfor(Message replyfor) {
        this.replyfor = replyfor;
    }

    public ReplyMessage(User userSender, User userReciver, String message, LocalDateTime data, List<User> users, Message replied) {
        super(userSender, userReciver, message, data, users);
        this.replyfor=replied;
    }
}
