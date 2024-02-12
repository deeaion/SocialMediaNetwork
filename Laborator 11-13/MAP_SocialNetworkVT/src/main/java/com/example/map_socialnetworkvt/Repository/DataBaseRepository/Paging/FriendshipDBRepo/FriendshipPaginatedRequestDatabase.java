package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FriendshipDBRepo;

import com.example.map_socialnetworkvt.Domain.Friendships.Friendship;
import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipRequest;
import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipStatus;
import com.example.map_socialnetworkvt.Domain.Tuple;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Exception.RepositoryException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.FriendshipDBRepo.FriendshipRequestDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.FilterMethod;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Page;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.PageImplementation;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Pageable;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.PagingRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.DataBaseAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class FriendshipPaginatedRequestDatabase extends FriendshipRequestDatabase implements
        PagingRepository<Tuple<Long,Long>,FriendshipRequest> {
    public FriendshipPaginatedRequestDatabase(Validator validator, DataBaseAccess data, String table) {
        super(validator, data, table);
    }


    @Override
    public Page<FriendshipRequest> findAll(Pageable pageable, FilterMethod<FriendshipRequest> friendshipRequestFilterMethod) {
        String findAllStatement="SELECT * FROM  GetFriendshipRequestInformation()";
        String whereC=friendshipRequestFilterMethod.getSqlWhereClause();
        if(whereC!=null)
        {
            findAllStatement+=whereC;
        }
        else
        {
            findAllStatement+=" ";
        }
        findAllStatement+="limit ? offset ?";

        Set<FriendshipRequest> friendships=new HashSet<>();
        try
        {
            PreparedStatement statement= data.createStatement(findAllStatement);
            int index=1;
            if(!friendshipRequestFilterMethod.getValues().isEmpty())
            {  for (Object o : friendshipRequestFilterMethod.getValues()) {
                statement.setObject(index, o);
                index++;
            }}
            statement.setInt(index++,pageable.getPageSize());
            statement.setInt(index,(pageable.getPageNumber()-1)*pageable.getPageSize());
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next())
            {
                FriendshipRequest friendship=getFriendshipRequestFromStatement(resultSet);
                friendships.add(friendship);
            }
            data.closeConnection();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        Stream<FriendshipRequest> friendshipStream=friendships.stream();
        return new PageImplementation<>(pageable,friendshipStream);
    }


    }

