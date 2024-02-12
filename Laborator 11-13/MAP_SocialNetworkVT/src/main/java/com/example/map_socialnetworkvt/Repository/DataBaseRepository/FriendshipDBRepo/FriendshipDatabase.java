package com.example.map_socialnetworkvt.Repository.DataBaseRepository.FriendshipDBRepo;

import com.example.map_socialnetworkvt.Domain.Friendships.Friendship;
import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipStatus;
import com.example.map_socialnetworkvt.Domain.Tuple;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Exception.RepositoryException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.DataBaseAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDatabase extends AbstractDataBaseRepository<Tuple<Long,Long>, Friendship> {
    public FriendshipDatabase(Validator<User> validator, DataBaseAccess data, String table) {
        super(validator, data, table);
    }

    @Override
    public Optional<Friendship> findOne(Tuple<Long,Long> id) {
        if (id==null)
        {
            throw new IllegalArgumentException("Id of entity is  null!");
        }
        String findOneStatement="SELECT * FROM  GetFriendshipInformation()" +
                " WHERE (id_user1=? and id_user2=?)" +
                " or (id_user2=? and id_user1=?) ;";
        try
        {
            PreparedStatement statement=data.createStatement(findOneStatement);
            statement.setLong(1,id.getLeft());
            statement.setLong(2,id.getRight());
            statement.setLong(3,id.getLeft());
            statement.setLong(4,id.getRight());
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next())
            {
                   Friendship friendship=getFriendshipFromStatement(resultSet);
                return Optional.of(friendship);
            }
            data.closeConnection();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public  Optional<Friendship> delete(Tuple<Long,Long> id)
    {
        Optional<Friendship> entity=findOne(id);
        if(id!=null)
        {
            String deleteStatement="DELETE FROM friendship where (id_user1=? and id_user2=?)" +
                    "or (id_user1=? and id_user2=?)";
            int response=0;
            try
            {
                PreparedStatement statement=data.createStatement(deleteStatement);
                statement.setLong(1,id.getLeft());

                statement.setLong(2,id.getRight());
                statement.setLong(4,id.getLeft());

                statement.setLong(3,id.getRight());

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
    protected Friendship getFriendshipFromStatement(ResultSet resultSet) throws SQLException
    {
            Long id_user1 = resultSet.getLong("id_user1");
            Long id_user2 = resultSet.getLong("id_user2");
            String firstName1 = resultSet.getString("firstNameU1");
            String lastName1 = resultSet.getString("lastNameU1");
            String username1 = resultSet.getString("usernameU1");
             String password1=resultSet.getString("passwordU1");
            User user1 = new User(firstName1, lastName1, username1,password1);
            user1.setId(id_user1);
            String firstName2 = resultSet.getString("firstNameU2");
            String lastName2 = resultSet.getString("lastNameU2");
            String username2 = resultSet.getString("usernameU2");
             String password2=resultSet.getString("passwordU2");
            User user2 = new User(firstName2, lastName2, username2,password2);
            user2.setId(id_user2);

            Timestamp friendsfrom = resultSet.getTimestamp("friendsfrom");

        return new Friendship(user1, user2, friendsfrom.toLocalDateTime());
        }
    @Override
    public Iterable<Friendship> findAll() {
        String findAllStatement="SELECT * FROM  GetFriendshipInformation()";
        Set<Friendship> friendships=new HashSet<>();
        try
        {
            PreparedStatement statement= data.createStatement(findAllStatement);
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
        return friendships;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        String insertSQL="INSERT INTO friendship(id_user1,id_user2,friendsfrom) values(?,?,?)";
        validator.vaildate(entity);

        try
        {
            PreparedStatement statement= data.createStatement(insertSQL);
            statement.setLong(1,entity.getId().getLeft());
            statement.setLong(2,entity.getId().getRight());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
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
    public Optional<Friendship> update(Friendship entity) {
        if (entity == null) {
            throw new RepositoryException("Enitity must not be null");
        }
        validator.vaildate(entity);

        String updateStatement = "UPDATE friendship set friendsfrom=?" +
                " where (id_user1=? and id_user2=?)" +
                "or (id_user2=? and id_user1=?)";
        try {
            PreparedStatement statement = data.createStatement(updateStatement);
            statement.setTimestamp(1,Timestamp.valueOf(entity.getDate()));

            statement.setLong(2,entity.getId().getLeft());
            statement.setLong(3,entity.getId().getRight());
            statement.setLong(4,entity.getId().getLeft());
            statement.setLong(5,entity.getId().getRight());;
            int response=statement.executeUpdate();
            data.closeConnection();
            return response==0? Optional.of(entity):Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

    }
}
