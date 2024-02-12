package com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Factory;

import com.example.map_toysocngradlefx2.Domain.Validators.*;
import com.example.map_toysocngradlefx2.Exception.RepositoryException;
//import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.MessageRepo.MessageDatabaseRepository;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.MessageRepo.MessageDatabaseRepository;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.UserDatabaseRepository;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Util.DataBaseAccess;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.FriendshipDBRepo.FriendshipDatabase;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.MessageRepo.ReplyMessageDatabaseRepository;
//import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.MessageRepo.ReplyMessageDatabaseRepository;

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

            default -> throw new RepositoryException("Invalid Strategy");
        }
    }


}
