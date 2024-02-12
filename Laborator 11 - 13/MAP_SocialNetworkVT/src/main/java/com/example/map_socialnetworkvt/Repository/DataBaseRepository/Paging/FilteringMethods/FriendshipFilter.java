package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods;

import com.example.map_socialnetworkvt.Domain.Friendships.Friendship;
import com.example.map_socialnetworkvt.Domain.User;

import java.util.Optional;

public class FriendshipFilter extends CreateFilter<Friendship> {
    Optional<User> forWhom=Optional.empty();

    public void setForWhom(Optional<User> forWhom) {
        this.forWhom = forWhom;
    }

    @Override
    protected void generateCommand() {
        if(forWhom.isPresent())
        {commands.add("id_user1 =?");
        commands.add("id_user2= ?");}

    }

    @Override
    protected void generateValues() {
        if(forWhom.isPresent())
        { values.add(forWhom.get().getId());
        values.add(forWhom.get().getId());}


    }
}
