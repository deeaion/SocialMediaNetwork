package com.example.map_socialnetworkvt.Service;

import com.example.map_socialnetworkvt.Domain.Friendships.Friendship;
import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipRequest;
import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipStatus;
import com.example.map_socialnetworkvt.Domain.Tuple;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Exception.ServiceException;
import com.example.map_socialnetworkvt.Repository.Repository;
import com.example.map_socialnetworkvt.Utils.Events.ChangeEventType;
import com.example.map_socialnetworkvt.Utils.Events.SocialNetworkChangeEvent;
import com.example.map_socialnetworkvt.Utils.Observer.Observable;
import com.example.map_socialnetworkvt.Utils.Observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipsService extends Service implements Observable<SocialNetworkChangeEvent> {
    public Set<User> set;
    Repository friendshipRequests;
    public FriendshipsService(Repository userRepo, Repository friendshipRepo,
                              Repository friendshipRequests) {
        super(userRepo, friendshipRepo);
        this.friendshipRequests=friendshipRequests;

    }

//ONLY ABOUT FRIENDSHIPS
    public boolean addFriendship(String username1, String username2) {

        User user1=findbyUsername(username1);
        User user2=findbyUsername(username2);
        Friendship friendship=new Friendship(user1, user2, LocalDateTime.now());
        //creez friendship dar trebuie sa il si adaug
        friendshipRepo.save(friendship);
        notifyObservers(new SocialNetworkChangeEvent(ChangeEventType.ADDFRIEND,friendship));
        return true;
    }
    public boolean modifyFriendship(String username1, String username2, LocalDateTime time)
    {
        User user1=findbyUsername(username1);
        User user2=findbyUsername(username2);
        Friendship found=findFriendship(user1,user2);
        Friendship newFr=new Friendship(user1,user2,time);
        friendshipRepo.update(newFr);
        notifyObservers(new SocialNetworkChangeEvent(ChangeEventType.UPDATEFRIEND,newFr));
        return true;
    }
    public Friendship findFriendship(User user1, User user2) {

        Tuple<Long,Long> id=new Tuple<>(user1.getId(),user2.getId());
        Optional<Friendship> foundF=friendshipRepo.findOne(id);
        return foundF.orElse(null);


    }
    public Iterable<Friendship> getAllFriendships()
    {
        return friendshipRepo.findAll();
    }
    public Iterable<FriendshipRequest> getAllRequests()
    {
        return friendshipRequests.findAll();
    }
    public boolean removeFriendship(String username1, String username2) {
        User user1=findbyUsername(username1);
        User user2=findbyUsername(username2);

        Friendship friendship=findFriendship(user1,user2);
        if(friendship==null)
        {
            throw new ServiceException("The friendship was not found!:(");
        }
        friendshipRepo.delete(friendship.getId());
        notifyObservers(new SocialNetworkChangeEvent(ChangeEventType.DELETEFRIEND,null));

        return true;
    }
    public List<User> getUsersFriends(User u)
    {
        List<Optional<User>> friends=new ArrayList<>();
        Iterable <Friendship> friendships=friendshipRepo.findAll();

        return StreamSupport.stream(friendships.spliterator(),false).
                filter(friendship -> Objects.equals(friendship.getId().getRight(), u.getId()) || Objects.equals(friendship.getId().getLeft(), u.getId()))
                .map(friendship ->
                {
                    if(Objects.equals(friendship.getId().getLeft(), u.getId()))
                        return userRepo.findOne(friendship.getId().getRight()).orElse(null);
                    else
                        return userRepo.findOne(friendship.getId().getLeft()).orElse(null);
                }).filter(Objects::nonNull).map(element->(User)element).collect(Collectors.toList());
    }

    //FRIENDSHIP REQUESTS
    public boolean addFriendshipRequest(String username1,String username2)
    {
        User user1=findbyUsername(username1);
        User user2=findbyUsername(username2);
        if(findFriendshipRequest(user1,user2)!=null)
        {
            throw new ServiceException("Friendship is already pending!");
        }
        FriendshipRequest request=new FriendshipRequest(user1,user2, FriendshipStatus.PENDING);
        friendshipRequests.save(request);
        notifyObservers(new SocialNetworkChangeEvent(ChangeEventType.ADDFRIEND,request));
        return true;
    }
    public FriendshipRequest findFriendshipRequest(User user1,User user2)
    {
        Iterable<FriendshipRequest> requests=friendshipRequests.findAll();
        List<FriendshipRequest> seeIfExists=StreamSupport.stream(requests.spliterator(),false)
                .filter( r->
                        r.getUser1().equals(user1)
                                &&r.getUser2().equals(user2)
                                &&r.getStatus()==FriendshipStatus.PENDING).
                map(r->(FriendshipRequest) r).toList();
        if(seeIfExists.size()==1)
        {
           return seeIfExists.get(0);
        }
        else
        {
            return null;
        }
    }
    public boolean rejectFriendship(String username1,String username2)
    {
        //username 1-> de la cine vine
        User user1=findbyUsername(username1);
        User user2=findbyUsername(username2);
        //username 2-> catre cine
        FriendshipRequest request=findFriendshipRequest(user1,user2);
        if(request!=null)
        {
            friendshipRequests.delete(request.getId());
;        }
        else
        {
            throw new ServiceException("Friend Request does not exist!");

        }
        return true;
    }

    public void acceptFriendship(String username1,String username2)
    {
        User user1=findbyUsername(username1);
        User user2=findbyUsername(username2);
        //username 2-> catre cine
        FriendshipRequest request=findFriendshipRequest(user1,user2);
        if(request!=null)
        {
            if(request.getStatus()==FriendshipStatus.PENDING)
            {
                request.setStatus(FriendshipStatus.ACCEPTED);
                friendshipRequests.update(request);
                addFriendship(username1,username2);
                notifyObservers(new SocialNetworkChangeEvent<>(ChangeEventType.ACCEPTREQUEST,request));
            }
        }
        else
        {
            throw new ServiceException("Friend Request does not exist!");

        }
    }
    public void removeFriendshipRequest(String username1,String username2) {
        User user1 = findbyUsername(username1);
        User user2 = findbyUsername(username2);
        FriendshipRequest request = findFriendshipRequest(user1, user2);
        if (request != null) {
            friendshipRequests.delete(request.getId());
            notifyObservers(new SocialNetworkChangeEvent<>(ChangeEventType.REJECTREQUEST, request));

        } else {
            throw new ServiceException("Friend Request does not exist!");

        }
    }








    //OBSERVERS

    private List<Observer<SocialNetworkChangeEvent>> observers=new ArrayList<>();
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
//used in paginated repo

}
