package com.example.map_socialnetworkvt.ServicePaginated;

import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.UserFilter;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FriendshipDBRepo.FriendshipPaginatedDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.PageableImplementation;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.UserPaginatedDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class ServicePag {
    protected final UserPaginatedDatabaseRepository userRepo;
    protected final FriendshipPaginatedDatabase friendshipRepo;
    public ServicePag(UserPaginatedDatabaseRepository userRepo, FriendshipPaginatedDatabase friendshipRepo)
    {
        this.userRepo=userRepo;
        this.friendshipRepo=friendshipRepo;
    }
    public User findbyUsername(String username)
    {
        UserFilter userFilter=new UserFilter();
        userFilter.setFiltering(username);
        userFilter.setOperator("or");
        List<User> found=userRepo.findAll(new PageableImplementation(1,10),userFilter).getContent().toList();
        if(found.isEmpty())
            return null;
        return found.get(0);


    }
    public User foundById(Long ID)
    {

        return (User) userRepo.findOne(ID).orElse(null);
    }

}