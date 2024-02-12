package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.MessageRepo;

import com.example.map_socialnetworkvt.Domain.Message.Message;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Exception.RepositoryException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.MessageRepo.MessageDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.FilterMethod;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Page;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.PageImplementation;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Pageable;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.PagingRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.DataBaseAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class MessagePaginatedDatabaseRespository extends MessageDatabaseRepository implements PagingRepository<Long,Message> {
    AbstractMessageDBRepo function=new AbstractMessageDBRepo();
    public MessagePaginatedDatabaseRespository(Validator validator, DataBaseAccess data, String table) {
        super(validator, data, table);
    }


    @Override
    public Page<Message> findAll(Pageable pageable, FilterMethod<Message> filterMethod) {
        Map<Long, Message> messageMap = new HashMap<>();

        String findAllStatement = "SELECT * FROM getMessageInfo()";
        String whereC=filterMethod.getSqlWhereClause();
        if(whereC!=null)
        {
            findAllStatement+=whereC;
        }
        else
        {
            findAllStatement+=" ";
        }
        findAllStatement+="limit ? offset ?";

        try {

            PreparedStatement statement = data.createStatement(findAllStatement);
            int index=1;
            if(!filterMethod.getValues().isEmpty())
            {  for (Object o : filterMethod.getValues()) {
                statement.setObject(index, o);
                index++;
            }}
            statement.setInt(index++,pageable.getPageSize());
            statement.setInt(index,(pageable.getPageNumber()-1)*pageable.getPageSize());
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
        return new PageImplementation<>(pageable,messageMap.values().stream());
    }


}
