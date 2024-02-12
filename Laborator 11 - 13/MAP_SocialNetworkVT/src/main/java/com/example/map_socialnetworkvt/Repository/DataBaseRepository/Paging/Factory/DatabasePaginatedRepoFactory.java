package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Factory;

import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Exception.RepositoryException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Factory.DataBaseRepositoryStrategy;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.FriendshipDBRepo.FriendshipDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.FriendshipDBRepo.FriendshipRequestDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.MessageRepo.MessageDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.MessageRepo.ReplyMessageDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FriendshipDBRepo.FriendshipPaginatedDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FriendshipDBRepo.FriendshipPaginatedRequestDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.MessageRepo.MessagePaginatedDatabaseRespository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.MessageRepo.ReplyMessagePaginatedDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.UserPaginatedDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.UserDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.DataBaseAccess;
//import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.ReplyMessageDatabaseRepository;

public class DatabasePaginatedRepoFactory implements DataBasePaginatedFactory {
   private final DataBaseAccess data;

    public DatabasePaginatedRepoFactory(DataBaseAccess data) {
        this.data=data;
    }
    @Override
    public AbstractDataBaseRepository createRepositor(DataBaseRepositoryStrategy strategy, Validator validator) {
        switch (strategy)
        {
            case users -> {return new UserPaginatedDatabaseRepository(validator,data,strategy.toString());}
            case friendship -> {return new FriendshipPaginatedDatabase(validator,data,strategy.toString());}
            case replymessage -> {return new ReplyMessagePaginatedDatabaseRepository(validator,data,strategy.toString());}
           case message -> {return new MessagePaginatedDatabaseRespository(validator,data,strategy.toString());}
            case request -> {return new FriendshipPaginatedRequestDatabase(validator,data,strategy.toString());
            }
            default -> throw new RepositoryException("Invalid Strategy");
        }
    }


}
