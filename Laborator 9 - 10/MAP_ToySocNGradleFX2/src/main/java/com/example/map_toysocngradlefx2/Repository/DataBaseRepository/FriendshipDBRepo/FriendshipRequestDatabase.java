package com.example.map_toysocngradlefx2.Repository.DataBaseRepository.FriendshipDBRepo;

import com.example.map_toysocngradlefx2.Domain.Friendships.Friendship;
import com.example.map_toysocngradlefx2.Domain.Friendships.FriendshipRequest;
import com.example.map_toysocngradlefx2.Domain.Friendships.FriendshipStatus;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipRequestDatabase extends AbstractDataBaseRepository<Tuple<Long,Long>, FriendshipRequest> {
    public FriendshipRequestDatabase(Validator validator, DataBaseAccess data, String table) {
        super(validator, data, table);
    }

    @Override
    public Optional<FriendshipRequest> findOne(Tuple<Long, Long> id) {
        if (id==null)
        {
            throw new IllegalArgumentException("Id of entity is  null!");
        }
        String findOneStatement="SELECT * FROM  GetFriendshipRequestInformation()" +
                " WHERE (id_requestSender=? and id_requestReciever=?)" +
                " or (id_requestReciever=? and id_requestSender=?) ;";
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
                FriendshipRequest friendship=getFriendshipRequestFromStatement(resultSet);
                return Optional.of(friendship);
            }
            data.closeConnection();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private FriendshipRequest getFriendshipRequestFromStatement(ResultSet resultSet) throws SQLException {

        Long id_user1 = resultSet.getLong("id_requestSender");
        Long id_user2 = resultSet.getLong("id_requestReciever");
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

        FriendshipStatus status=FriendshipStatus.valueOf(resultSet.getString("status"));
        return new FriendshipRequest(user1,user2,status);
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        String findAllStatement="SELECT * FROM  GetFriendshipRequestInformation()";
        Set<FriendshipRequest> friendships=new HashSet<>();
        try
        {
            PreparedStatement statement= data.createStatement(findAllStatement);
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
        return friendships;
    }

    @Override
    public Optional<FriendshipRequest> save(FriendshipRequest entity) {
        String insertSQL="INSERT INTO friendshipRequest(id_requestSender,id_requestReciever,status) values(?,?,?)";
        validator.vaildate(entity);

        try
        {
            PreparedStatement statement= data.createStatement(insertSQL);
            statement.setLong(1,entity.getId().getLeft());
            statement.setLong(2,entity.getId().getRight());
            statement.setString(3,entity.getStatus().toString());
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
    public Optional<FriendshipRequest> delete(Tuple<Long, Long> id) {
        Optional<FriendshipRequest> entity=findOne(id);
        if(id!=null)
        {
            String deleteStatement="DELETE FROM friendshipRequest "+"WHERE (id_requestSender=? and id_requestReciever=?)" +
            " or (id_requestReciever=? and id_requestSender=?) ;";
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

    @Override
    public Optional<FriendshipRequest> update(FriendshipRequest entity) {
        if (entity == null) {
            throw new RepositoryException("Enitity must not be null");
        }
        validator.vaildate(entity);

        String updateStatement = "UPDATE friendshipRequest set status=?" +
                "WHERE (id_requestSender=? and id_requestReciever=?)" +
                " or (id_requestReciever=? and id_requestSender=?) ;";
        try {
            PreparedStatement statement = data.createStatement(updateStatement);
            statement.setString(1,entity.getStatus().toString());
            statement.setLong(2,entity.getId().getLeft());
            statement.setLong(3,entity.getId().getRight());
            statement.setLong(5,entity.getId().getLeft());
            statement.setLong(4,entity.getId().getRight());
            int response=statement.executeUpdate();
            data.closeConnection();
            return response==0? Optional.of(entity):Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

    }
    }

