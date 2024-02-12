package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods;

import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipRequest;
import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipStatus;
import com.example.map_socialnetworkvt.Domain.User;

import java.util.Optional;

public class RequestFilter extends CreateFilter<FriendshipRequest>{
    Optional<User> userReciver=Optional.empty();
    Optional<FriendshipStatus> status=Optional.empty();
    //user reciever is id_user1

    public void setUserReciver(Optional<User> userReciver) {
        this.userReciver = userReciver;
    }
    public void setStatus(Optional<FriendshipStatus> status){
        this.status=status;
    }

    @Override
    protected void generateCommand() {
        userReciver.ifPresent(user -> commands.add("id_user2= ?"));
        status.ifPresent(status->commands.add("status= ?"));
    }

    @Override
    protected void generateValues() {
        userReciver.ifPresent(user -> values.add(user.getId()));
        status.ifPresent(status->values.add(status.toString()));

    }
}
