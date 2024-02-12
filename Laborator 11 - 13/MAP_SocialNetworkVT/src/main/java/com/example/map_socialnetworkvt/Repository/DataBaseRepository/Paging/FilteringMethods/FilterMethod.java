package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods;

import com.example.map_socialnetworkvt.Domain.Entity;

import java.util.List;

public interface FilterMethod<E> {

    String getSqlWhereClause();
    List<String> getCommands();
    void setCommands(List<String> commands);
    void setValues(List<Object> values);
    List<Object> getValues();

    void setOperator(String operator);
    String getOperator();


}
