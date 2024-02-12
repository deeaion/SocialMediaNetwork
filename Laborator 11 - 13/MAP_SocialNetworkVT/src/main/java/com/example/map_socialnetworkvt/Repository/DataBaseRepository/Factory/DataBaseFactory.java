package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Factory;

import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;

public interface DataBaseFactory {
    /**
     * Creates Factory for strategy
     * @param strategy Strategy that decides type of Validator
     * @return DataBaseFactory conform to strategy
     */
    AbstractDataBaseRepository createRepositor(DataBaseRepositoryStrategy strategy, Validator validator);
}
