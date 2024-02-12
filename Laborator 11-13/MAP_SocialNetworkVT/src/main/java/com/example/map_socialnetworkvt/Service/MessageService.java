package com.example.map_socialnetworkvt.Service;

import com.example.map_socialnetworkvt.Domain.Message.Message;
import com.example.map_socialnetworkvt.Domain.Message.ReplyMessage;
import com.example.map_socialnetworkvt.Domain.Tuple;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Exception.ServiceException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Pageable;
import com.example.map_socialnetworkvt.Repository.Repository;
import com.example.map_socialnetworkvt.Utils.Events.ChangeEventType;
import com.example.map_socialnetworkvt.Utils.Events.SocialNetworkChangeEvent;
import com.example.map_socialnetworkvt.Utils.Observer.Observable;
import com.example.map_socialnetworkvt.Utils.Observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageService extends Service implements Observable<SocialNetworkChangeEvent> {
    private final Repository messageRepo;
    private final Repository replyRepo;
    protected List<Observer<SocialNetworkChangeEvent>> observers=new ArrayList<>();

    public MessageService(Repository userRepo, Repository friendshipRepo,Repository messageRepo,Repository replyRepo) {
        super(userRepo, friendshipRepo);
        this.messageRepo=messageRepo;
        this.replyRepo=replyRepo;
    }
    public List<Message> getAllMessage()
    {
        return (List<Message>) messageRepo.findAll();
    }
    public List<ReplyMessage> getReplies()
    {
        return (List<ReplyMessage>) replyRepo.findAll();
    }
    public List<Message> getMessageForUser(String username)
    {
       User user=findbyUsername(username);
       if(user==null)
       {
           return null;
       }
       List<Message> allMessages=getAllMessage();
       List<Message> messages= StreamSupport.stream(allMessages.spliterator(),false).
                                filter(m->{
                                    return m.getUsers().contains(user);
                                }).collect(Collectors.toList());
       return messages;
    }
    public List<Message> getMessagesFromU1toU2(String username1,String username2)
    {
        User user2=findbyUsername(username2);
        List<Message> messages=getMessageForUser(username1);
        return StreamSupport.stream(messages.spliterator(),false).filter(
                message -> {
                   return message.getUserSender().getUsername().equals(user2.getUsername());
                }
                ).collect(Collectors.toList());
    }
    public List<ReplyMessage> getRepliesofUser(String username)
    {
        User user=findbyUsername(username);
        if(user==null)
        {
            return null;
        }
        List<ReplyMessage> replyMessages=getReplies();
        List<ReplyMessage> messages= StreamSupport.stream(replyMessages.spliterator(),false).
                filter(m->{
                    return Objects.equals(m.getUserSender().getUsername(), user.getUsername());
                }).collect(Collectors.toList());
        return messages;
    }
    public List<ReplyMessage> getRepliesofUser1toUser2(String username1,String username2)
    {
        List<ReplyMessage> replyMessages=getRepliesofUser(username1);
        return StreamSupport.stream(replyMessages.spliterator(),false).
                filter(replyMessage -> {
                    return replyMessage.getReplyfor().getUserSender().getUsername().equals(username2);
                }).collect(Collectors.toList());
    }
    public List<String> getConvo(String username1,String username2)
    {
       User user1=findbyUsername(username1);
       User user2=findbyUsername(username2);
       if(user1==null)
       {
           throw new ServiceException("First user does not exist");
       }
        if(user2==null)
        {
            throw new ServiceException("Second user does not exist");
        }
        List<Message> user1MessagesFromUser2=getMessagesFromU1toU2(username1,username2);
        List<Message> user2MessagesFromUser1=getMessagesFromU1toU2(username2,username1);
        List<ReplyMessage> user1RepliesToUser2=getRepliesofUser1toUser2(username1,username2);
        List<ReplyMessage> user2RepliesToUser1=getRepliesofUser1toUser2(username2,username1);
        //acu incepem convo cu primul user care trimite deci trebuie sa vedem data
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(user1MessagesFromUser2);
        allMessages.addAll(user2MessagesFromUser1);
        Collections.sort(allMessages, Comparator.comparing(Message::getData));

        List<ReplyMessage> allReplies = new ArrayList<>();
        allReplies.addAll(user1RepliesToUser2);
        allReplies.addAll(user2RepliesToUser1);
        Collections.sort(allReplies, Comparator.comparing(ReplyMessage::getData));
        List<Tuple<String, LocalDateTime>> messages=new ArrayList<>();
        int i=0;
        for (Message message : allMessages) {
            messages.add(new Tuple(message.getUserSender().getUsername() + ": " + message.getMessage(),message.getData()));
        }

        for (ReplyMessage reply : allReplies) {
            if (reply.getReplyfor() != null) {

                while(i<messages.size()&&reply.getData().compareTo(messages.get(i).getRight())>=0)
                {
                    i++;
                }
                String mes="\t\t" + reply.getUserSender().getUsername() + " replied to "
                        + reply.getReplyfor().getUserSender().getUsername() + ": " + reply.getMessage();
                messages.add(i,new Tuple(mes,reply.getData()));
                i++;
            }
        }
        List<String> convo=StreamSupport.stream(messages.spliterator(),false).map(tuple->
        {return tuple.getLeft();}).collect(Collectors.toList());
        return convo;
    }
    private List<User>getUsers(String users)
    {
        List<User> usersF=new ArrayList<>();
        String[] words = users.split(",");
        for (String userName:words)
        {
            User user=findbyUsername(userName);
            if(user==null)
            {
                throw new ServiceException("User "+userName+" does not exist!");
            }
            usersF.add(user);

        }
        return usersF;
    }
    public void addMessage(String username1,String text,String users)
    {
        User sender=findbyUsername(username1);
        List<User> userList=getUsers(users);
        Message message=new Message(sender,text,LocalDateTime.now(),userList);
        messageRepo.save(message);
        notifyObservers(new SocialNetworkChangeEvent(ChangeEventType.ADDMESSAGE,message));

    }
    public void deleteMessage(Long id)
    {
       messageRepo.delete(id);

    }
    public void sendReply(Long idMessage,String reply,String usernameReciever)
    {
        User reciever=findbyUsername(usernameReciever);
        Message findMessageToReplyto=(Message) messageRepo.findOne(idMessage).orElse(null);
        if(findMessageToReplyto.getUsers().contains(reciever))
        {
            List<User> setUser=new ArrayList<>();
            setUser.add(findMessageToReplyto.getUserSender());
            ReplyMessage replyMessage=new ReplyMessage(reciever,reply,LocalDateTime.now(),setUser,findMessageToReplyto);
            replyRepo.save(replyMessage);
        }
        else
        {
            throw new ServiceException("The user does not reply to this message!");
        }


    }
    public void deleteReply(Long id)
    {
        replyRepo.delete(id);
    }
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
        observers.stream().forEach(x->x.update(t));
    }
}
