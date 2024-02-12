package com.example.map_socialnetworkvt.Repository.DataBaseRepository.MessageRepo;

import com.example.map_socialnetworkvt.Domain.Message.Message;
import com.example.map_socialnetworkvt.Domain.Message.ReplyMessage;
import com.example.map_socialnetworkvt.Domain.Tuple;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Exception.RepositoryException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.DataBaseAccess;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;


public class ReplyMessageDatabaseRepository extends AbstractDataBaseRepository<Long, ReplyMessage> {
    AbstractMessageDBRepo function=new AbstractMessageDBRepo();

    public ReplyMessageDatabaseRepository(Validator validator, DataBaseAccess data, String table) {
        super(validator, data, table);
    }
    private Message getReplyMessageM(Long id)
    {
        if (id == null) {
            throw new IllegalArgumentException("Id of entity is  null!");
        }
        String findOneStatement = "SELECT * FROM getMessage()" +
                " WHERE id_message=?";
        try(Connection connection= DriverManager.getConnection(data.getUrl(),data.getUsernameDB(), data.getPassword())) {
            PreparedStatement statement = connection.prepareStatement(findOneStatement);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Message message = function.getMessageFromStatement(resultSet);
                return message;
            }
          //  data.closeConnection();
            return null;
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }}
        private Message getOtherMessage(Long id)
    {

        String message="SELECT * from getMessageInfo() WHERE id_message=?";
        try(Connection connection= DriverManager.getConnection(data.getUrl(),data.getUsernameDB(), data.getPassword())) {
            Message messageR=null;
            PreparedStatement statement=connection.prepareStatement(message);
            statement.setLong(1,id);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next())
            {
                messageR=function.getMessageFromStatement(resultSet);
            }
           // data.closeConnection();
            return messageR;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected Optional<ReplyMessage> getReplyMessage(ResultSet resultSet) throws SQLException {
        ReplyMessage reply;
        Long id_reply=resultSet.getLong("id_reply");
        Message replytoMessage=getReplyMessageM(id_reply);

        Long id_message=resultSet.getLong("id_message");
        Message messagetoReplyto=getOtherMessage(id_message);
        reply=new ReplyMessage(replytoMessage.getUserSender(),
                replytoMessage.getMessage(),replytoMessage.getData(),
                replytoMessage.getUsers(),messagetoReplyto);
        reply.setId(id_reply);
        return Optional.of(reply);
    }

    @Override
    public Optional<ReplyMessage> findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id of entity is  null!");
        }
        String findOneStatement = "SELECT * FROM replyMessage" +
                " WHERE id_reply=?";
        try {
            PreparedStatement statement = data.createStatement(findOneStatement);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
            {
               return getReplyMessage(resultSet);

            }
            data.closeConnection();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<ReplyMessage> findAll() {
        List <ReplyMessage> messageMap = new ArrayList<>();

        String findAllStatement = "SELECT * FROM replyMessage";
        try {

            PreparedStatement statement = data.createStatement(findAllStatement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                Optional<ReplyMessage> replyMessage=getReplyMessage(resultSet);
                replyMessage.ifPresent(messageMap::add);
            }
            data.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return messageMap;
    }
    private ReplyMessage saveMessage(ReplyMessage entity) throws SQLException
    {
        String insertMessage=" INSERT INTO message"+
                "(id_sender,message,data) VALUES (?,?,?)";
        PreparedStatement statement=data.createStatement(insertMessage);
        statement.setLong(1,entity.getUserSender().getId());
        statement.setString(2,entity.getMessage());
        statement.setTimestamp(3,Timestamp.valueOf(entity.getData()));
        int response=statement.executeUpdate();
        data.closeConnection();
        if(response==0)
        {
            return null;
        }

        String getId="SELECT MAX(id_message) FROM message";
        PreparedStatement statementId=data.createStatement(getId);
        ResultSet resultSet=statementId.executeQuery();
        if (resultSet.next())
        {
            long id=resultSet.getLong("max");
            entity.setId(id);

        }
        data.closeConnection();
        return entity;
    }
    @Override
    public Optional<ReplyMessage> save(ReplyMessage entity) {
        //validator.vaildate(entity);

        try
        {
            entity=saveMessage(entity);
            if(entity!=null)
            {String insertSQL="INSERT INTO " +
                    "replymessage(id_message,id_reply) values(?,?)";
            PreparedStatement statement= data.createStatement(insertSQL);
            statement.setLong(1,entity.getReplyfor().getId());
            statement.setLong(2,entity.getId());
            int response=statement.executeUpdate();
            data.closeConnection();
            return response==0?Optional.of(entity):Optional.empty();}
            else
            {
                throw new RepositoryException("Could not save the response!");
            }
        }
        catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<ReplyMessage> delete(Long id) {
        Optional<ReplyMessage> entity=findOne(id);
        if(id!=null)
        {
            String deleteStatement="DELETE FROM replymessage where id_reciever=?";
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
    public Optional<ReplyMessage> update(ReplyMessage entity) {
//        if (entity == null) {
//            throw new RepositoryException("Enitity must not be null");
//        }
//        //validator.vaildate(entity);
//
//        String updateStatement = "UPDATE replymessage set messageReply=?, dataR=?" +
//                " where (id_message=? and id_reciever=?)";
//        try {
//            PreparedStatement statement = data.createStatement(updateStatement);
//            statement.setString(1,entity.getReply());
//            statement.setTimestamp(2,Timestamp.valueOf(entity.getDataReciever()));
//            statement.setLong(3,entity.getId().getLeft());
//            statement.setLong(4,entity.getId().getRight());
//            int response=statement.executeUpdate();
//            data.closeConnection();
//            return response==0? Optional.of(entity):Optional.empty();
//        } catch (SQLException e) {
//            throw new RepositoryException(e);
//        }
//    }
        return Optional.empty();
}}
