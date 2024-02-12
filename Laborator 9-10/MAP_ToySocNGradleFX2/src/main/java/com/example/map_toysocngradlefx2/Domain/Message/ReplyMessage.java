package com.example.map_toysocngradlefx2.Domain.Message;

import com.example.map_toysocngradlefx2.Domain.Entity;
import com.example.map_toysocngradlefx2.Domain.Tuple;
import com.example.map_toysocngradlefx2.Domain.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class ReplyMessage extends Entity<Tuple<Long,Long>> {
    private Message replyfor;
    private User reciever;
    private LocalDateTime dataReciever;
    private String reply;

    public ReplyMessage(Message replyfor, User reciever, LocalDateTime dataReciever, String reply) {
        this.replyfor = replyfor;
        this.reciever = reciever;
        this.dataReciever = dataReciever;
        this.reply = reply;
        Long idM=replyfor.getId();
        Long idU=reciever.getId();
        Tuple<Long, Long> Tuple=new Tuple(idM,idU);
        this.setId(Tuple);
    }

    @Override
    public String toString() {
        return "ReplyMessage{" +
                "replyfor=" + replyfor.getUserSender().getUsername() +
                ", reciever=" + reciever.getUsername() +
                ", dataReciever=" + dataReciever.toString() +
                ", reply='" + reply + '\'' +
                '}';
    }

    public Message getReplyfor() {
        return replyfor;
    }

    public void setReplyfor(Message replyfor) {
        this.replyfor = replyfor;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public LocalDateTime getDataReciever() {
        return dataReciever;
    }

    public void setDataReciever(LocalDateTime dataReciever) {
        this.dataReciever = dataReciever;
    }

    public String getReply() {
        return reply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReplyMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getReplyfor(), that.getReplyfor()) && Objects.equals(getReciever(), that.getReciever()) && Objects.equals(getDataReciever(), that.getDataReciever()) && Objects.equals(getReply(), that.getReply());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getReplyfor(), getReciever(), getDataReciever(), getReply());
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
