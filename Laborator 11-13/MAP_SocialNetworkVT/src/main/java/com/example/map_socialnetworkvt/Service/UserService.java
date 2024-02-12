package com.example.map_socialnetworkvt.Service;

import com.example.map_socialnetworkvt.Domain.Entity;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Exception.ServiceException;
import com.example.map_socialnetworkvt.Repository.Repository;
import com.example.map_socialnetworkvt.Service.Utils.PasswordUtils;
import com.example.map_socialnetworkvt.Utils.Events.ChangeEventType;
import com.example.map_socialnetworkvt.Utils.Events.SocialNetworkChangeEvent;
import com.example.map_socialnetworkvt.Utils.Observer.Observable;
import com.example.map_socialnetworkvt.Utils.Observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService extends Service implements Observable<SocialNetworkChangeEvent>{

    private final String type;

    public UserService(Repository userRepo, Repository friendshipRepository, String type) {
       super(userRepo,friendshipRepository);
        this.type=type;
    }
    /*
    Add/Remove User
     */

    public boolean addUser(String firstName, String lastName, String userName,String password) {
        String hashedpassword=PasswordUtils.hashPassword(password);
        User NewUser=new User(firstName,lastName,userName,hashedpassword);
        userRepo.save(NewUser);

        notifyObservers(new SocialNetworkChangeEvent(ChangeEventType.ADD,NewUser));
        return true;
    }
    public boolean verifyPassword(String password,User user)
    {
        return PasswordUtils.verifyPassword(password,user.getPassword());
    }
    public Entity<Long> removeUser(String username) {
        User foundUser=findbyUsername(username);
        //REMOVE THEM FROM FRIENDSHIPS!
        if(foundUser==null)
        {
            throw new ServiceException("User was not found!");
        }
        Optional<Entity<Long>> deletedUser=userRepo.delete(foundUser.getId());
        User deleted= (User) deletedUser.get();
        notifyObservers(new SocialNetworkChangeEvent(ChangeEventType.ADD,deleted));
        return deleted;

    }

    public boolean updateUser(String firstName,String lastName,String username,String password)
    {
        User existent=findbyUsername(username);
        User newUser=new User(firstName,lastName,username,password);
        if(existent==null){
            throw new ServiceException("User was not found!");
        }
        newUser.setId(existent.getId());
        boolean updated=!userRepo.update(newUser).isPresent();
        if(updated)
        {
            notifyObservers(new SocialNetworkChangeEvent(ChangeEventType.UPDATE,newUser));

        }
        return updated;
    }

    protected List<Observer<SocialNetworkChangeEvent>> observers=new ArrayList<>();

    @Override
    public void addObserver(Observer<SocialNetworkChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<SocialNetworkChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(SocialNetworkChangeEvent t) {
        observers.forEach(x->x.update(t));
    }



    /**
     * Get all users
     * @return users
     */

    public Iterable getAllUsers() {
        return userRepo.findAll();
    }

    /**
     * Get all communities
     * @return communities of people
     */


}
