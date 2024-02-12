package com.example.map_socialnetworkvt.Repository.DataBaseRepository.MessageRepo;

import com.example.map_socialnetworkvt.Domain.Message.Message;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Exception.RepositoryException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.DataBaseAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDatabaseRepository extends AbstractDataBaseRepository<Long, Message> {
    AbstractMessageDBRepo function=new AbstractMessageDBRepo();
    public MessageDatabaseRepository(Validator validator, DataBaseAccess data, String table) {
        super(validator, data, table);
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
                Message message = function.getMessageFromStatement(resultSet);
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
                    LocalDateTime dateTime = resultSet.getTimestamp("data").toLocalDateTime();
                    String message = resultSet.getString("message");
                    User sender = function.getOwner(resultSet);
                    List<User> user = new ArrayList<>();
                    User reciever = function.getReciever(resultSet);
                    user.add(reciever);
                    Message messageT = new Message(sender, message, dateTime, user);
                    messageT.setId(id_Message);
                    messageMap.put(id_Message, messageT);
                }
                else {
                    User rec = function.getReciever(resultSet);
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

    private boolean insertIntoMessageReciever(Long mes,User user)
    {
        String insertSQL="INSERT INTO messageReciever (id_message,id_reciever) VALUES (?,?)";
        try
        {
            PreparedStatement statement= data.createStatement(insertSQL);
            statement.setLong(1,mes);
            statement.setLong(2,user.getId());
            int response=statement.executeUpdate();
            data.closeConnection();
            return response == 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            Long getLastMessageInserted=getLastMessage();
            if(response==1)
            {
                //inseamna ca am reusit
                insertRecievers(entity.getUsers());
            }
            return response==0?Optional.of(entity):Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    private void insertRecievers(List<User> users) {
        Long getLastMes=getLastMessage();
        users.forEach(u->{insertIntoMessageReciever(getLastMes,u);
                            });
    }

    private Long getLastMessage() {
        String getIdStatement="SELECT MAX(id_message) FROM message";
        try
        {
            PreparedStatement statement=data.createStatement(getIdStatement);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next())
            {
                return resultSet.getLong("max");
            }
            else
            {
                throw new RepositoryException("Something went wrong when trying to get Message!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
