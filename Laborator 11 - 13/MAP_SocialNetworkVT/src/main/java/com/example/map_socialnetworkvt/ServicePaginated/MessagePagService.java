package com.example.map_socialnetworkvt.ServicePaginated;

import com.example.map_socialnetworkvt.Domain.Entity;
import com.example.map_socialnetworkvt.Domain.Message.Message;
import com.example.map_socialnetworkvt.Domain.Message.ReplyMessage;
import com.example.map_socialnetworkvt.Domain.Tuple;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Exception.ServiceException;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.MessageFilter;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.ReplyFilter;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FriendshipDBRepo.FriendshipPaginatedDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.MessageRepo.MessagePaginatedDatabaseRespository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.MessageRepo.ReplyMessagePaginatedDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Pageable;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.PageableImplementation;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.UserPaginatedDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.Repository;
import com.example.map_socialnetworkvt.Utils.Events.ChangeEventType;
import com.example.map_socialnetworkvt.Utils.Events.SocialNetworkChangeEvent;
import com.example.map_socialnetworkvt.Utils.Observer.Observable;
import com.example.map_socialnetworkvt.Utils.Observer.Observer;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessagePagService extends ServicePag implements Observable<SocialNetworkChangeEvent> {
    private final MessagePaginatedDatabaseRespository messageRepo;
    private final ReplyMessagePaginatedDatabaseRepository replyRepo;
    private List<Observer<SocialNetworkChangeEvent>> observers=new ArrayList<>();

    public MessagePagService(UserPaginatedDatabaseRepository userRepo,
                             FriendshipPaginatedDatabase friendshipRepo, MessagePaginatedDatabaseRespository messageRepo, ReplyMessagePaginatedDatabaseRepository replyRepo) {
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
        MessageFilter messageFilter=new MessageFilter();
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
    private static int compareMessages(Message message1, Message message2) {
        // Compare messages, considering null values
        if (message1 == null && message2 == null) {
            return 0; // Both are null, consider them equal
        } else if (message1 == null) {
            return -1; // message1 is null, consider it "less than" message2
        } else if (message2 == null) {
            return 1; // message2 is null, consider it "greater than" message1
        } else {
            // Compare based on your logic, for example, using message1.getDate().compareTo(message2.getDate())
            return Objects.requireNonNull(message1.getData()).compareTo(Objects.requireNonNull(message2.getData()));
        }
    }
    public String initiateConvoLastMessage(String username1, String username2)
    {
        //last messages username 1
        User user1=findbyUsername(username1);
        User user2=findbyUsername(username2);
        Message lastMessageUser1=getLastMessage(user1,user2);

        Message lastMessageUser2=getLastMessage(user2,user1);

        ReplyMessage lastReply=getLastReply(user1,user2);
        ReplyMessage lastReply2=getLastReply(user2,user1);
        int lastMessageComparison = compareMessages(lastMessageUser1, lastMessageUser2);

        // Compare last reply messages
        int lastReplyComparison = compareMessages(lastReply, lastReply2);
        if(lastMessageComparison==0)
        {
           return "";
        }
        if(lastMessageComparison>0)
        {
            //inseamna ca message 1 e corect
            if(lastReplyComparison>0)
            {
                if(compareMessages(lastMessageUser1,lastReply)>0)
                    return username1+":"+lastMessageUser1.getMessage();
                else
                    return username1+":"+lastReply.getMessage();

            }
            else
            {
                if(compareMessages(lastMessageUser1,lastReply2)>0)
                    return username1+":"+lastMessageUser1.getMessage();
                else
                    return username2+":"+lastReply2.getMessage();
            }
        }
        else
        {
            if(lastReplyComparison>0)
            {
                if(compareMessages(lastMessageUser2,lastReply)>0)

                    return username2+":"+lastMessageUser2.getMessage();
                else
                    return username1+":"+lastReply.getMessage();

            }
            else
            {
                if(compareMessages(lastMessageUser2,lastReply2)>0)

                    return username2+":"+lastMessageUser2.getMessage();
                else
                    return username2+":"+lastReply2.getMessage();
            }
        }

    }


    public Message getLastMessage(User userSender,User reciever)
    {
        MessageFilter filter=new MessageFilter();
        filter.setId_reciever(reciever);
        filter.setId_sender(userSender);
        filter.setOrder('>');
        filter.setOperator("and");
        List<Message> found=getPagedMessages(1,1,filter);
        if (found.isEmpty()) return null;
        return found.get(0);
    }

    public ReplyMessage getLastReply(User userReciever,User whoSendsReply)
    {
       ReplyFilter filter=new ReplyFilter();
        filter.setOrder(Optional.of('>'));
        boolean done=false;
        int page=1;
        int items=5;
        ReplyMessage reply=null;
        List<ReplyMessage> all=new ArrayList<>();
        while (!done)
        {
            List<ReplyMessage> found=getPagedRMessages(page,items,filter);
            if(found.size()<items)
                done=true;
            found = found.stream()
                    .filter(replyMessage ->
                            replyMessage.getUserSender().getUsername().equals(whoSendsReply.getUsername()) &&
                                    replyMessage.getReplyfor().getUserSender().getUsername().equals(userReciever.getUsername()))
                  // Sort by date in descending order
                    .collect(Collectors.toList());
            if(found.size()>1)
            {
                all.addAll(found);
            }
                page++;
                found=getPagedRMessages(page,items,filter);

        }
        if(all.size()==0)
            return null;
        all=all.stream().sorted(Comparator.comparing(ReplyMessage::getData).reversed()).toList(); // Sort by date in descending order;
        return all.get(0);
    }


    public List<Message> getPagedMessages(int page, int objNumber, MessageFilter filter)
    {
        return messageRepo.findAll(new PageableImplementation(page,objNumber),filter).getContent().collect(Collectors.toList());
    }
    public List<ReplyMessage> getPagedRMessages(int page, int objNumber, ReplyFilter filter)
    {
        return replyRepo.findAll(new PageableImplementation(page,objNumber),filter).getContent().collect(Collectors.toList());
    }
    public List<Object> getConvoObjects(String username1, String username2) {
        User user1 = findbyUsername(username1);
        User user2 = findbyUsername(username2);
        if (user1 == null) {
            throw new ServiceException("First user does not exist");
        }
        if (user2 == null) {
            throw new ServiceException("Second user does not exist");
        }

        List<Message> user1MessagesFromUser2 = getMessagesFromU1toU2(username1, username2);
        List<Message> user2MessagesFromUser1 = getMessagesFromU1toU2(username2, username1);
        List<ReplyMessage> user1RepliesToUser2 = getRepliesofUser1toUser2(username1, username2);
        List<ReplyMessage> user2RepliesToUser1 = getRepliesofUser1toUser2(username2, username1);

        List<Object> convo = new ArrayList<>();

        // Add messages from user1 to user2
        convo.addAll(user1MessagesFromUser2);

        // Add messages from user2 to user1
        convo.addAll(user2MessagesFromUser1);

        // Add reply messages from user1 to user2
        convo.addAll(user1RepliesToUser2);

        // Add reply messages from user2 to user1
        convo.addAll(user2RepliesToUser1);

        // Sort all messages and replies by timestamp
        Collections.sort(convo, (o1, o2) -> {
            LocalDateTime timestamp1 = (o1 instanceof Message) ? ((Message) o1).getData() : ((ReplyMessage) o1).getData();
            LocalDateTime timestamp2 = (o2 instanceof Message) ? ((Message) o2).getData() : ((ReplyMessage) o2).getData();
            return timestamp1.compareTo(timestamp2);
        });

        return convo;
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
            messages.add(new Tuple(message.getUserSender().getUsername() + ":" + message.getMessage(),message.getData()));
        }

        for (ReplyMessage reply : allReplies) {
            if (reply.getReplyfor() != null) {

                while(i<messages.size()&&reply.getData().compareTo(messages.get(i).getRight())>=0)
                {
                    i++;
                }
                String mes=reply.getUserSender().getUsername() + "replied to "
                        + reply.getReplyfor().getUserSender().getUsername() + ": " + reply.getMessage();
                messages.add(i,new Tuple(mes,reply.getData()));
                i++;
            }
        }
        List<String> convo=StreamSupport.stream(messages.spliterator(),false).map(tuple->
        {return tuple.getLeft();}).collect(Collectors.toList());
        return convo;
    }


    public Map<User, String> getConvoEntries(User user) {
        Map<User, String> convos = new HashMap<>();
        Iterable<Message> messages = messageRepo.findAll();

        List<User> interactedMessages = new ArrayList<>();
        messages.forEach(message ->
                {
                    if(message.getUserSender().equals(user))
                    {
                        interactedMessages.addAll(message.getUsers());
                    }
                    else if(message.getUsers().contains(user))
                    {
                        interactedMessages.add(message.getUserSender());
                    }
                }
        );
        Iterable<ReplyMessage> replyMessages = replyRepo.findAll();
        replyMessages.forEach(message ->
                {
                    if(message.getUserSender().equals(user))
                    {
                        interactedMessages.add(message.getReplyfor().getUserSender());
                    }
                }
        );

        interactedMessages.forEach(
                interacted -> {
                    convos.put(interacted, "p");
                }
        );

        convos.keySet().forEach(k -> {
            convos.put(k, initiateConvoLastMessage(user.getUsername(), k.getUsername()));
        });

        return convos;
    }
    public List<User>getUsers(String users)
    {
        List<User> usersF=new ArrayList<>();
        String[] words = users.split(",");
        for (String userName:words)
        {
            if(!userName.isEmpty())
            { User user=findbyUsername(userName);
            if(user==null)
            {
                throw new ServiceException("User "+userName+" does not exist!");
            }
            usersF.add(user);}

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
            notifyObservers(new SocialNetworkChangeEvent(ChangeEventType.ADDREPLY,replyMessage));

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
