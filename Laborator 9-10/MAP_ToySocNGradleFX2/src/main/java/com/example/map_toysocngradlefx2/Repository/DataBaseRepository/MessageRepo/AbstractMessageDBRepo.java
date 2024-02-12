package com.example.map_toysocngradlefx2.Repository.DataBaseRepository.MessageRepo;

import com.example.map_toysocngradlefx2.Domain.Message.Message;
import com.example.map_toysocngradlefx2.Domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMessageDBRepo {
    protected User getOwner(ResultSet resultSet) throws SQLException {
        Long id_sender = resultSet.getLong("id_sender");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        User sender = new User(firstName, lastName, username, password);
        sender.setId(id_sender);
        return sender;
    }
    private User getReciever(ResultSet resultSet) throws SQLException {
        Long id_reciver = resultSet.getLong("id_reciever");
        String firstNameR = resultSet.getString("first_nameR");
        String lastNameR = resultSet.getString("last_nameR");
        String usernameR = resultSet.getString("usernameR");
        String passwordR = resultSet.getString("passwordR");

        User reciever = new User(firstNameR, lastNameR, usernameR, passwordR);
        reciever.setId(id_reciver);
        return reciever;
    }
    private Message getMessageFromStatement(ResultSet resultSet) throws SQLException {
        //getting data of message
        Long id_mes = resultSet.getLong("id_message");
        LocalDateTime dateTime = resultSet.getTimestamp("data").toLocalDateTime();
        String message = resultSet.getString("message");
        //creating sender
        User sender = getOwner(resultSet);

        //celalalt user
        List<User> users = new ArrayList<>();
        User reciever = getReciever(resultSet);
        users.add(reciever);
        //continui
        while (resultSet.next()) {

            reciever = getReciever(resultSet);
            users.add(reciever);
        }
        Message messageNew = new Message(sender, message, dateTime, users);
        messageNew.setId(id_mes);
        return messageNew;
    }

}
