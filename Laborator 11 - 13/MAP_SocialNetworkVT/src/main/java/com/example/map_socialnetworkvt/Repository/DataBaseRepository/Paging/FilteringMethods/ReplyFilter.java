package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods;

import com.example.map_socialnetworkvt.Domain.Message.Message;
import com.example.map_socialnetworkvt.Domain.Message.ReplyMessage;
import com.example.map_socialnetworkvt.Domain.User;

import java.util.Optional;

public class ReplyFilter extends CreateFilter<ReplyMessage> {
    private Optional<Character> order=Optional.empty();

    public void setOrder(Optional<Character> order) {
        this.order = order;
    }

    @Override
    protected void generateCommand() {

        if(order.isPresent())
        {
            if(order.get().equals('>'))
            {
                if(commands.isEmpty())
                {
                    commands.add("ORDER BY data DESC");
                }

            }

        }
    }

    @Override
    protected void generateValues() {

    }
}
