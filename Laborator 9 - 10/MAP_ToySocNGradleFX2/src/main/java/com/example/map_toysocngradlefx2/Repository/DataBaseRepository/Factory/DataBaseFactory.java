package com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Factory;

import com.example.map_toysocngradlefx2.Domain.Validators.Validator;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;

public interface DataBaseFactory {
    /**
     * Creates Factory for strategy
     * @param strategy Strategy that decides type of Validator
     * @return DataBaseFactory conform to strategy
     */
    AbstractDataBaseRepository createRepositor(DataBaseRepositoryStrategy strategy, Validator validator);
}
