package com.example.map_toysocngradlefx2.Service;

import com.example.map_toysocngradlefx2.Domain.Message.Message;
import com.example.map_toysocngradlefx2.Domain.Message.ReplyMessage;
import com.example.map_toysocngradlefx2.Domain.Tuple;
import com.example.map_toysocngradlefx2.Domain.User;
import com.example.map_toysocngradlefx2.Exception.ServiceException;
import com.example.map_toysocngradlefx2.Repository.Repository;
import com.example.map_toysocngradlefx2.Utils.Events.ChangeEventType;
import com.example.map_toysocngradlefx2.Utils.Events.SocialNetworkChangeEvent;
import com.example.map_toysocngradlefx2.Utils.Observer.Observable;
import com.example.map_toysocngradlefx2.Utils.Observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageService extends Service implements Observable<SocialNetworkChangeEvent> {
    private final Repository messageRepo;
    private final Repository replyRepo;
    private List<Observer<SocialNetworkChangeEvent>> observers=new ArrayList<>();

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
                    return Objects.equals(m.getReciever().getUsername(), user.getUsername());
                }).collect(Collectors.toList());
        return messages;
    }
    public List<ReplyMessage> getRepliesofUser1toUser2(String username1,String username2)
    {
        List<ReplyMessage> replyMessages=getRepliesofUser(username1);
        return StreamSupport.stream(replyMessages.spliterator(),false).
                filter(replyMessage -> {
                    return replyMessage.getReplyfor().getUserSender().getUsername().equals(username2)
                            &&replyMessage.getReply()!=null;
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
        Collections.sort(allReplies, Comparator.comparing(ReplyMessage::getDataReciever));
        List<Tuple<String, LocalDateTime>> messages=new ArrayList<>();
        int i=0;
        for (Message message : allMessages) {
            messages.add(new Tuple(message.getUserSender().getUsername() + ": " + message.getMessage(),message.getData()));
        }

        for (ReplyMessage reply : allReplies) {
            if (reply.getReplyfor() != null) {

                while(i<messages.size()&&reply.getDataReciever().compareTo(messages.get(i).getRight())>0)
                {
                    i++;
                }
                String mes="\t\t" + reply.getReciever().getUsername() + " replied to "
                        + reply.getReplyfor().getUserSender().getUsername() + ": " + reply.getReply();
                messages.add(i,new Tuple(mes,reply.getDataReciever()));
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
        String[] words = users.split(" ");
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
       if (sender==null)
       {
           throw new ServiceException("User "+username1+" does not exist!");
       }
       List<User> userList=getUsers(users);
        Message message=new Message(sender,text,LocalDateTime.now(),userList);
        if(messageRepo.save(message).isEmpty())
        {
            Message finalMessage = message;
           Iterable<Message> allmesajes=messageRepo.findAll();
            List<Message> mesL= StreamSupport.stream(allmesajes.spliterator(),false)
                    .filter(mes-> mes.getUserSender().getUsername().equals(finalMessage.getUserSender().getUsername()))
                    .filter(mes-> mes.getUsers().get(0).getFirstName()==null&&mes.getUsers().get(0).getUsername()==null).
                    collect(Collectors.toList());
            if(mesL.size()!=0)
                message=mesL.get(0);
        }
        for(User userE:userList)
        {
            ReplyMessage reply=new ReplyMessage(message,userE,LocalDateTime.now(),null);
            replyRepo.save(reply);
        }

        notifyObservers(new SocialNetworkChangeEvent(ChangeEventType.ADDMESSAGE,message));

    }
    public void deleteMessage(Long id)
    {
       messageRepo.delete(id);

    }
    public void sendReply(Long idMessage,String reply,String usernameReciever)
    {
        User reciever=findbyUsername(usernameReciever);

        Tuple<Long,Long> id=new Tuple<>(idMessage,reciever.getId());
        Message message=(Message) messageRepo.findOne(idMessage).orElse(null);
        ReplyMessage replyMessage=new ReplyMessage(message,reciever,LocalDateTime.now(),reply);
        replyRepo.update(replyMessage);
    }
    public void deleteReply(Tuple<Long,Long> id)
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
