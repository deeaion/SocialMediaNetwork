package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods;

import com.example.map_socialnetworkvt.Domain.Entity;

import java.util.ArrayList;
import java.util.List;

public abstract class CreateFilter <E extends Entity> implements FilterMethod<E> {

    protected List<Object> values = new ArrayList<>();
    protected List<String> commands = new ArrayList<>();
    protected String sqlWhereClause = null;
    protected String operator;

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }
    @Override
    public void setCommands(List<String> commands) {
        this.commands=commands;
    }
    @Override

    public void setValues(List<Object> values) {
        values.addAll(this.values);
    }


    public String getSqlWhereClause() {
        sqlWhereClause=null;
        commands = new ArrayList<>();
        values=new ArrayList<>();
        generateSqlWhereClause();
        return sqlWhereClause;
    }

    @Override
    public List<String> getCommands() {
        return commands;
    }

    @Override
    public List<Object> getValues() {
        return values;
    }

    protected abstract void generateCommand();

    protected abstract void generateValues();

    protected void generateSqlWhereClause() {
        generateCommand();
        generateValues();
        if (!commands.isEmpty()) {
            if(values.size()>1)
                sqlWhereClause=" where ";
            else
                sqlWhereClause=" ";
            String toAdd= String.join(" " + operator + " ", commands);
            sqlWhereClause+=toAdd+" ";
        }
    }
}
