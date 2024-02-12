package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Factory;

import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Factory.DataBaseRepositoryStrategy;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;

public interface DataBasePaginatedFactory {
    /**
     * Creates Factory for strategy
     * @param strategy Strategy that decides type of Validator
     * @return DataBaseFactory conform to strategy
     */
    AbstractDataBaseRepository createRepositor(DataBaseRepositoryStrategy strategy, Validator validator);
}
