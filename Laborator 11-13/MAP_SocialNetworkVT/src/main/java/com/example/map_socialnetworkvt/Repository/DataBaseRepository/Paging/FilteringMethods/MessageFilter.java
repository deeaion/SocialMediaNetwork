package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods;

import com.example.map_socialnetworkvt.Domain.Message.Message;
import com.example.map_socialnetworkvt.Domain.User;

import java.util.Optional;

public class MessageFilter extends CreateFilter<Message> {
    private Optional<Character> order=Optional.empty();
    private Optional<User> id_sender=Optional.empty();
    private Optional<User> id_reciever=Optional.empty();
    public void setOrder(Character order)
    {
        this.order=Optional.of(order);
    }
    public void setId_sender(User userSender)
    {
        this.id_sender= Optional.ofNullable(userSender);
    }
    public void setId_reciever(User userReciever)
    {
        this.id_reciever=Optional.of(userReciever);
    }


    @Override
    protected void generateCommand() {

        if(id_sender.isPresent())
        {
            commands.add("id_sender= ?");
        }
        if(id_reciever.isPresent())
        {
            commands.add("id_reciever= ?");
        }
        if(order.isPresent())
        {
            if(order.get().equals('>'))
            {
                if(commands.isEmpty())
                {
                    commands.add("ORDER BY data DESC");
                }
                else
                {
                    commands.add(commands.size()-1,commands.get(commands.size()-1)+" ORDER BY data DESC");
                    commands.remove(commands.size()-1);
                }
            }

        }
    }

    @Override
    protected void generateValues() {
        id_sender.ifPresent(user -> values.add(user.getId()));
        id_reciever.ifPresent(user -> values.add(user.getId()));

    }
}
