package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FriendshipDBRepo;

import com.example.map_socialnetworkvt.Domain.Friendships.Friendship;
import com.example.map_socialnetworkvt.Domain.Tuple;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Exception.RepositoryException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.FriendshipDBRepo.FriendshipDatabase;
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
import java.util.*;
import java.util.stream.Stream;

public class FriendshipPaginatedDatabase extends FriendshipDatabase implements PagingRepository<Tuple<Long,Long>,Friendship> {
    public FriendshipPaginatedDatabase(Validator<User> validator, DataBaseAccess data, String table) {
        super(validator, data, table);
    }

    @Override
    public Page<Friendship> findAll(Pageable pageable, FilterMethod<Friendship> friendshipFilterMethod) {
        String findAllStatement="SELECT * FROM  GetFriendshipInformation()";
        String whereC=friendshipFilterMethod.getSqlWhereClause();
        if(whereC!=null)
        {
            findAllStatement+=whereC;
        }
        else
        {
            findAllStatement+=" ";
        }
        findAllStatement+="limit ? offset ?";

        List<Friendship> friendships=new ArrayList<>();
        try
        {
            PreparedStatement statement= data.createStatement(findAllStatement);
            int index=1;
            if(!friendshipFilterMethod.getValues().isEmpty())
            {  for (Object o : friendshipFilterMethod.getValues()) {
                statement.setObject(index, o);
                index++;
            }}
            statement.setInt(index++,pageable.getPageSize());
            statement.setInt(index,(pageable.getPageNumber()-1)*pageable.getPageSize());
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next())
            {
                Friendship friendship=getFriendshipFromStatement(resultSet);
                friendships.add(friendship);
            }
            data.closeConnection();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        Stream<Friendship> friendshipStream=friendships.stream();
        return new PageImplementation<>(pageable,friendshipStream);
    }
}
