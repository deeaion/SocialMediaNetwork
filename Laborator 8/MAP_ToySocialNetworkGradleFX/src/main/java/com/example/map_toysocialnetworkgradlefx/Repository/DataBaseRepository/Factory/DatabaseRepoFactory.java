package com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Factory;

import com.example.map_toysocialnetworkgradlefx.Domain.Validators.*;
import com.example.map_toysocialnetworkgradlefx.Exception.RepositoryException;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.UserDatabaseRepository;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Util.DataBaseAccess;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.FriendshipDatabase;

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
            default -> throw new RepositoryException("Invalid Strategy");
        }
    }


}
