package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods;

import com.example.map_socialnetworkvt.Domain.User;

import java.util.List;

public class UserFilter extends CreateFilter<User> {
    private String likeContains = "";

    public void setFiltering(String filter) {
        this.likeContains = filter;
    }

    @Override
    protected void generateCommand() {
        if(!likeContains.isEmpty())
        { commands.add("first_name like ?");
        commands.add("last_name like ?");
        commands.add("username like ?");}

    }

    @Override
    protected void generateValues() {
        if(!likeContains.isEmpty())
        {values.add("%" + likeContains + "%");
        values.add("%"+ likeContains+"%");
        values.add("%"+likeContains+"%");}
    }

}
