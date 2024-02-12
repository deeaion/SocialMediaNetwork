package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Factory;

import com.example.map_socialnetworkvt.Domain.Validators.*;
import com.example.map_socialnetworkvt.Exception.RepositoryException;
//import com.example.map_socialnetworkvt.Repository.DataBaseRepository.MessageDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.FriendshipDBRepo.FriendshipDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.FriendshipDBRepo.FriendshipRequestDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.MessageRepo.MessageDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.MessageRepo.ReplyMessageDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.UserDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.DataBaseAccess;
//import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.ReplyMessageDatabaseRepository;

public class DatabaseRepoFactory implements DataBaseFactory {
   private final DataBaseAccess data;

    public DatabaseRepoFactory(DataBaseAccess data) {
        this.data=data;
    }
    @Override
    public AbstractDataBaseRepository createRepositor(DataBaseRepositoryStrategy strategy,Validator validator) {
        switch (strategy)
        {
            case users -> {return new UserDatabaseRepository(validator,data,strategy.toString());}
            case friendship -> {return new FriendshipDatabase(validator,data,strategy.toString());}
            case replymessage -> {return new ReplyMessageDatabaseRepository(validator,data,strategy.toString());}
           case message -> {return new MessageDatabaseRepository(validator,data,strategy.toString());}
            case request -> {return new FriendshipRequestDatabase(validator,data,strategy.toString());
            }
            default -> throw new RepositoryException("Invalid Strategy");
        }
    }


}
