package com.example.map_toysocngradlefx2.Repository.DataBaseRepository.MessageRepo;

import com.example.map_toysocngradlefx2.Domain.Friendships.Friendship;
import com.example.map_toysocngradlefx2.Domain.Message.Message;
import com.example.map_toysocngradlefx2.Domain.User;
import com.example.map_toysocngradlefx2.Domain.Validators.Validator;
import com.example.map_toysocngradlefx2.Exception.RepositoryException;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Util.DataBaseAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDatabaseRepository extends AbstractDataBaseRepository<Long, Message> {
    public MessageDatabaseRepository(Validator validator, DataBaseAccess data, String table) {
        super(validator, data, table);
    }

    private User getOwner(ResultSet resultSet) throws SQLException {
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

    @Override
    public Optional<Message> findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id of entity is  null!");
        }
        String findOneStatement = "SELECT * FROM getMessageInfo()" +
                " WHERE id_message=?";
        try {
            PreparedStatement statement = data.createStatement(findOneStatement);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Message message = getMessageFromStatement(resultSet);
                return Optional.of(message);
            }
            data.closeConnection();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Message> findAll() {
        Map<Long, Message> messageMap = new HashMap<>();

        String findAllStatement = "SELECT * FROM getMessageInfo()";
        try {

            PreparedStatement statement = data.createStatement(findAllStatement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id_Message = resultSet.getLong("id_message");
                if (!messageMap.containsKey(id_Message)) {
//                    Long id_mes=resultSet.getLong("id_message");
                    LocalDateTime dateTime = resultSet.getTimestamp("data").toLocalDateTime();
                    String message = resultSet.getString("message");
                    User sender = getOwner(resultSet);
                    List<User> user = new ArrayList<>();
                    User reciever = getReciever(resultSet);
                    user.add(reciever);
                    Message messageT = new Message(sender, message, dateTime, user);
                    messageT.setId(id_Message);
                    messageMap.put(id_Message, messageT);
                } else {
                    User rec = getReciever(resultSet);
                    Message get = messageMap.get(id_Message);
                    List<User> useri = get.getUsers();
                    useri.add(rec);
                    get.setUsers(useri);
                    messageMap.put(id_Message, get);
                }
            }
            data.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(messageMap.values());
    }



    @Override
    public Optional<Message> save(Message entity) {
        String insertSQL="INSERT INTO message (id_sender, message, data) values(?,?,?)";
       // validator.vaildate(entity);

        try
        {
            PreparedStatement statement= data.createStatement(insertSQL);
            statement.setLong(1,entity.getUserSender().getId());
            statement.setString(2,entity.getMessage());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getData()));
            int response=statement.executeUpdate();
            data.closeConnection();
            return response==0?Optional.of(entity):Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Message> delete(Long id) {
        Optional<Message> entity=findOne(id);
        if(id!=null)
        {
            String deleteStatement="DELETE FROM message where id_message=?";
            int response=0;
            try
            {
                PreparedStatement statement=data.createStatement(deleteStatement);
                statement.setLong(1,id);

                if (entity.isPresent())
                {
                    response=statement.executeUpdate();
                }
                data.closeConnection();
                return response==0? Optional.empty() : entity;

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            throw  new IllegalArgumentException("ID cannot be null!");
        }
    }

    @Override
    public Optional<Message> update(Message entity) {
        if (entity == null) {
            throw new RepositoryException("Enitity must not be null");
        }
     //   validator.vaildate(entity);

        String updateStatement = "UPDATE message set message=?, data=?" +
                " where id_message=?";
        try {
            PreparedStatement statement = data.createStatement(updateStatement);
            statement.setString(1,entity.getMessage());
            statement.setTimestamp(2,Timestamp.valueOf(entity.getData()));
            statement.setLong(3,entity.getId());
            int response=statement.executeUpdate();
            data.closeConnection();
            return response==0? Optional.of(entity):Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}
