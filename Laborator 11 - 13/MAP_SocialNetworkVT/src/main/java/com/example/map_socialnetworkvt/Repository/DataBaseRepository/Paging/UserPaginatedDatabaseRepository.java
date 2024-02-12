package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging;

import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Exception.RepositoryException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.FilterMethod;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.UserFilter;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.UserDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.DataBaseAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserPaginatedDatabaseRepository extends UserDatabaseRepository
implements PagingRepository<Long,User>{
    public UserPaginatedDatabaseRepository(Validator<User> validator, DataBaseAccess data, String table) {
        super(validator, data, table);
    }

    @Override
    public Page<User> findAll(Pageable pageable, FilterMethod<User> filterMethod) {
        String findAllStatement="SELECT * FROM users";
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
        Set<User> users=new HashSet<>();
        try
        {
            PreparedStatement statement= data.createStatement(findAllStatement);
            int index=1;
            if(!filterMethod.getValues().isEmpty())
            {  for (Object o : filterMethod.getValues()) {
                statement.setObject(index, o);
                index++;
            }}
            statement.setInt(index++,pageable.getPageSize());
            statement.setInt(index,(pageable.getPageNumber()-1)*pageable.getPageSize());
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next())
            {
                Long id=resultSet.getLong("id");
                users.add(getUser(resultSet,id).get());

            }
            data.closeConnection();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return new PageImplementation<>(pageable,users.stream());
    }



}
