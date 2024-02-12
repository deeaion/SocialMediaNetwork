package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.MessageRepo;

import com.example.map_socialnetworkvt.Domain.Message.Message;
import com.example.map_socialnetworkvt.Domain.Message.ReplyMessage;
import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Exception.RepositoryException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.MessageRepo.ReplyMessageDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.FilterMethod;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Page;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.PageImplementation;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Pageable;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.PagingRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.DataBaseAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ReplyMessagePaginatedDatabaseRepository extends ReplyMessageDatabaseRepository
implements PagingRepository<Long,ReplyMessage> {
    AbstractMessageDBRepo function=new AbstractMessageDBRepo();

    public ReplyMessagePaginatedDatabaseRepository(Validator validator, DataBaseAccess data, String table) {
        super(validator, data, table);
    }
    @Override
    public Page<ReplyMessage> findAll(Pageable pageable, FilterMethod<ReplyMessage> replyMessageFilterMethod) {
        List <ReplyMessage> messageMap = new ArrayList<>();

        String findAllStatement = "SELECT * FROM replyMessage";
      //  String whereC=replyMessageFilterMethod.getSqlWhereClause();

//        if(whereC!=null)
//        {
//            findAllStatement+=whereC;
//        }
//        else
//        {
            findAllStatement+=" ";
        //}
        findAllStatement+="limit ? offset ?";

        try {

            PreparedStatement statement = data.createStatement(findAllStatement);
            int index=1;
            if(!replyMessageFilterMethod.getValues().isEmpty())
            {  for (Object o : replyMessageFilterMethod.getValues()) {
                statement.setObject(index, o);
                index++;
            }}
            statement.setInt(index++,pageable.getPageSize());
            statement.setInt(index,(pageable.getPageNumber()-1)*pageable.getPageSize());
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
        return new PageImplementation<>(pageable,messageMap.stream());
    }
 }
