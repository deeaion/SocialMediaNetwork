package com.example.map_socialnetworkvt.ServicePaginated;

import com.example.map_socialnetworkvt.Domain.Friendships.Friendship;
import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipRequest;
import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipStatus;
import com.example.map_socialnetworkvt.Domain.Tuple;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Exception.ServiceException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.FilterMethod;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.FriendshipFilter;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.RequestFilter;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FriendshipDBRepo.FriendshipPaginatedDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FriendshipDBRepo.FriendshipPaginatedRequestDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Page;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Pageable;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.PageableImplementation;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.UserPaginatedDatabaseRepository;
import com.example.map_socialnetworkvt.Utils.Events.ChangeEventType;
import com.example.map_socialnetworkvt.Utils.Events.SocialNetworkChangeEvent;
import com.example.map_socialnetworkvt.Utils.Observer.Observable;
import com.example.map_socialnetworkvt.Utils.Observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipsPagService extends ServicePag implements Observable<SocialNetworkChangeEvent> {


    FriendshipPaginatedRequestDatabase friendshipRequests;

    public FriendshipsPagService(UserPaginatedDatabaseRepository userRepo, FriendshipPaginatedDatabase friendshipRepo,
                              FriendshipPaginatedRequestDatabase friendshipRequests) {
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


    //looking with get all
    public List<User> getUsersFriendsPerPage(int page,int items,User u)
    {
        FriendshipFilter filter=new FriendshipFilter();
        filter.setForWhom(Optional.of(u));
        filter.setOperator("or");
        Pageable pageable=new PageableImplementation(page,items);
        Page thisPage=friendshipRepo.findAll(pageable,filter);
        List<Friendship> foundFriends=thisPage.getContent().toList();
        return foundFriends.stream()
                .map(friendship -> {
                    if(Objects.equals(friendship.getUser1().getUsername(), u.getUsername()))
                    {
                        return friendship.getUser2();
                    }
                    else
                    {
                        return friendship.getUser1();
                    }
                }).collect(Collectors.toList());

    }
    public List<User> getUsersFriends(User u)
    {
        List<User> friends=new ArrayList<>();

//        return StreamSupport.stream(friendships.spliterator(),false).
//                filter(friendship -> Objects.equals(friendship.getId().getRight(), u.getId()) || Objects.equals(friendship.getId().getLeft(), u.getId()))
//                .map(friendship ->
//                {
//                    if(Objects.equals(friendship.getId().getLeft(), u.getId()))
//                        return userRepo.findOne(friendship.getId().getRight()).orElse(null);
//                    else
//                        return userRepo.findOne(friendship.getId().getLeft()).orElse(null);
//                }).filter(Objects::nonNull).map(element->(User)element).collect(Collectors.toList());
        int page=1;
        int items=10;
        boolean done=false;
        while (!done)
        {
          List<User> foundFriends=getUsersFriendsPerPage(page,items,u);
            if(foundFriends.size()<items)
            {
                done=true;
            }
            friends.addAll(foundFriends);
            page++;
        }
        return friends;

    }

    //used in paginated repo



    public Iterable<Friendship> getAllFriendships()
    {
        int page=1;
        int items=10;
        Pageable pageable=new PageableImplementation(page,items);
        List<Friendship> friends=new ArrayList<>();
        FriendshipFilter filter=new FriendshipFilter();
        boolean done=false;
        while (!done)
        {
            List<Friendship> foundFriends= (List<Friendship>) getFriendshipsPaginated(page,items,filter);
            if(foundFriends.size()<items)
            {
                done=true;
            }
            friends.addAll(foundFriends);
            page++;
        }
        return friends;
    }

    //now . I use pagination for my frontend . So
    //I was thinking to have yk the pages and items i get from the main like there i am on page idk
    public Iterable<Friendship> getFriendshipsPaginated(int page,int items,FriendshipFilter filter)
    {

        Pageable pageable=new PageableImplementation(page,items);
        Page thisPage=friendshipRepo.findAll(pageable,filter);
        return (List<Friendship>) thisPage.getContent().toList();
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
        return friendshipRequests.findOne(new Tuple<>(user1.getId(),user2.getId())).orElse(null);
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
            friendshipRequests.delete(request.getId());}
        else
        {
            throw new ServiceException("Friend Request does not exist!");

        }
        notifyObservers(new SocialNetworkChangeEvent<>(ChangeEventType.REJECTREQUEST,request));
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
                friendshipRequests.delete(request.getId());
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




    public Iterable<FriendshipRequest> getAllRequestsPaginated(int page, int items, RequestFilter filter)
    {
        Pageable pageable=new PageableImplementation(page,items);
        Page thisPage=friendshipRequests.findAll(pageable,filter);
        List<FriendshipRequest> foundFriends=thisPage.getContent().toList();
        return foundFriends;

    }
    public Iterable<FriendshipRequest> getAllRequests()
    {
        int page=1;
        int items=10;
        List<FriendshipRequest> friends=new ArrayList<>();
        RequestFilter filter=new RequestFilter();
        boolean done=false;
        while (!done)
        {

            List<FriendshipRequest> foundFriends= (List<FriendshipRequest>) getAllRequestsPaginated(page,items,filter);
            if(foundFriends.size()<items)
            {
                done=true;
            }
            friends.addAll(foundFriends);
            page++;
        }
        return friends;
    }

    public Iterable<FriendshipRequest> getRequestsForUser(int page,int items,User user)
    {
        RequestFilter filter=new RequestFilter();
        filter.setUserReciver(Optional.ofNullable(user));
        filter.setStatus(Optional.of(FriendshipStatus.PENDING));
        filter.setOperator("and");
        return getAllRequestsPaginated(page,items,filter);
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
}