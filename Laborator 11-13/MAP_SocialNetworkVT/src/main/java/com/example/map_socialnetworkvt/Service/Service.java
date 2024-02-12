package com.example.map_socialnetworkvt.Service;

import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.UserDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.Repository;

import java.util.Optional;
import java.util.stream.StreamSupport;

public class  Service {
    protected final Repository userRepo;
    protected final Repository friendshipRepo;
    public Service(Repository userRepo,Repository friendshipRepo)
    {
        this.userRepo=userRepo;
        this.friendshipRepo=friendshipRepo;
    }
    public User findbyUsername(String username)
    {
        return (User) StreamSupport.stream(userRepo.findAll().spliterator(),false)
                .filter(user->((User)user).getUsername().equals(username))
                .findFirst()
                .orElse(null);

    }
    public User foundById(Long ID)
    {
        return (User) userRepo.findOne(ID).orElse(Optional.empty());
    }

}