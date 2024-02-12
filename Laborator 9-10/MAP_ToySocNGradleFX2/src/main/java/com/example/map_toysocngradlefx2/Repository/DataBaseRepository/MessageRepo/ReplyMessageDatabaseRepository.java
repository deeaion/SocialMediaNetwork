package com.example.map_toysocngradlefx2.Repository.DataBaseRepository.MessageRepo;

import com.example.map_toysocngradlefx2.Domain.Friendships.Friendship;
import com.example.map_toysocngradlefx2.Domain.Message.Message;
import com.example.map_toysocngradlefx2.Domain.Message.ReplyMessage;
import com.example.map_toysocngradlefx2.Domain.Tuple;
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


public class ReplyMessageDatabaseRepository extends AbstractDataBaseRepository<Tuple<Long,Long>, ReplyMessage> {

    public ReplyMessageDatabaseRepository(Validator validator, DataBaseAccess data, String table) {
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
    private Message getMessage(Long id)
    {
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
                return Optional.of(message).get();
            }
            data.closeConnection();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<ReplyMessage> findOne(Tuple<Long, Long> id) {
        if (id == null) {
            throw new IllegalArgumentException("Id of entity is  null!");
        }
        String findOneStatement = "SELECT * FROM getReplyMessageInfo()" +
                " WHERE (id_message=? and id_sender=?)";
        try {
            PreparedStatement statement = data.createStatement(findOneStatement);
            statement.setLong(1, id.getLeft());
            statement.setLong(2,id.getRight());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ReplyMessage reply;
                LocalDateTime dataResponse=resultSet.getTimestamp("dataR").toLocalDateTime();
                String messageReply=resultSet.getString("messageReply");

                Long id_message=resultSet.getLong("id_message");
                 Message getMes=getMessage(id_message);
                User reciever = getReciever(resultSet);
                reply=new ReplyMessage(getMes,reciever,dataResponse,messageReply);
                return Optional.of(reply);
            }
            data.closeConnection();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<ReplyMessage> findAll() {
        Map<Tuple<Long,Long>, ReplyMessage> messageMap = new HashMap<>();

        String findAllStatement = "SELECT * FROM getReplyMessageInfo()";
        try {

            PreparedStatement statement = data.createStatement(findAllStatement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                Long id_Message = resultSet.getLong("id_message");
                Long id_rec=resultSet.getLong("id_reciever");
                Tuple<Long,Long> id=new Tuple<>(id_Message,id_rec);
                if (!messageMap.containsKey(id)) {
    //                Long id_mes=resultSet.getLong("id_message");
                    ReplyMessage reply;
                    LocalDateTime dataResponse=resultSet.getTimestamp("dataR").toLocalDateTime();
                    String messageReply=resultSet.getString("messageReply");
                    Message getMes=getMessage(id_Message);
                    User reciever = getReciever(resultSet);
                    reply=new ReplyMessage(getMes,reciever,dataResponse,messageReply);
                    messageMap.put(id,reply);

                }
            }
            data.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(messageMap.values());
    }

    @Override
    public Optional<ReplyMessage> save(ReplyMessage entity) {

        String insertSQL="INSERT INTO replymessage(id_message,id_reciever,messageReply,dataR) values(?,?,?,?)";
        //validator.vaildate(entity);

        try
        {
            PreparedStatement statement= data.createStatement(insertSQL);
            statement.setLong(1,entity.getReplyfor().getId());
            statement.setLong(2,entity.getId().getRight());
            statement.setString(3, entity.getReply());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getDataReciever()));
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
    public Optional<ReplyMessage> delete(Tuple<Long, Long> id) {
        Optional<ReplyMessage> entity=findOne(id);
        if(id!=null)
        {
            String deleteStatement="DELETE FROM replymessage where id_message=? id_reciever=?";
            int response=0;
            try
            {
                PreparedStatement statement=data.createStatement(deleteStatement);
                statement.setLong(1,id.getLeft());
                statement.setLong(2,id.getRight());

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
    public Optional<ReplyMessage> update(ReplyMessage entity) {
        if (entity == null) {
            throw new RepositoryException("Enitity must not be null");
        }
        //validator.vaildate(entity);

        String updateStatement = "UPDATE replymessage set messageReply=?, dataR=?" +
                " where (id_message=? and id_reciever=?)";
        try {
            PreparedStatement statement = data.createStatement(updateStatement);
            statement.setString(1,entity.getReply());
            statement.setTimestamp(2,Timestamp.valueOf(entity.getDataReciever()));
            statement.setLong(3,entity.getId().getLeft());
            statement.setLong(4,entity.getId().getRight());
            int response=statement.executeUpdate();
            data.closeConnection();
            return response==0? Optional.of(entity):Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }}
