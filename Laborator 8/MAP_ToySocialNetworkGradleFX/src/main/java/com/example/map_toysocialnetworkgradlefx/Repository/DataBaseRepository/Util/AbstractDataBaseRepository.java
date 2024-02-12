package com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Util;

import com.example.map_toysocialnetworkgradlefx.Domain.Entity;
import com.example.map_toysocialnetworkgradlefx.Domain.Validators.Validator;
import com.example.map_toysocialnetworkgradlefx.Repository.Repository;

import java.util.Optional;

public abstract class AbstractDataBaseRepository <ID,E extends Entity<ID>> implements Repository<ID,E> {
    protected  Validator validator;
    protected DataBaseAccess data;
    protected String table;
    public AbstractDataBaseRepository(Validator validator,DataBaseAccess data,String table)
    {
        this.validator=validator;
        this.data=data;
        this.table=table;
    }
    @Override
    public  abstract Optional<E> findOne(ID id);

    @Override
    public abstract Iterable<E> findAll();

    @Override
    public abstract Optional<E> save(E entity) ;

    @Override
    public  abstract Optional<E> delete(ID id);

    @Override
    public abstract Optional<E> update(E entity) ;
}
