package com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Factory;

import com.example.map_toysocialnetworkgradlefx.Domain.Validators.Validator;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;

public interface DataBaseFactory {
    /**
     * Creates Factory for strategy
     * @param strategy Strategy that decides type of Validator
     * @return DataBaseFactory conform to strategy
     */
    AbstractDataBaseRepository createRepositor(DataBaseRepositoryStrategy strategy, Validator validator);
}
