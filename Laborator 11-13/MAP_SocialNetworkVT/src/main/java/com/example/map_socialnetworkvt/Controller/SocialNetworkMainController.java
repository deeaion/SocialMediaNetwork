





package com.example.map_socialnetworkvt.Controller;

import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Service.FriendshipsService;
import com.example.map_socialnetworkvt.Service.MessageService;
import com.example.map_socialnetworkvt.Service.UserService;
import com.example.map_socialnetworkvt.ServicePaginated.FriendshipsPagService;
import com.example.map_socialnetworkvt.ServicePaginated.MessagePagService;
import com.example.map_socialnetworkvt.ServicePaginated.UserPagService;
import com.example.map_socialnetworkvt.Utils.Events.SocialNetworkChangeEvent;
import com.example.map_socialnetworkvt.Utils.Observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SocialNetworkMainController implements Observer<SocialNetworkChangeEvent> {

    @FXML
    private Button btnFriends;

    @FXML
    private Button btnMessages;

    @FXML
    private Button btnNotifications;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnUsers;
    private UserPagService serviceUser;
    private FriendshipsPagService serviceFriendships;
    private MessagePagService serviceMessages;
    private User user;
    @FXML
    private Button btnExit;

    @FXML
    private SubScene middleScene;

    @FXML
    void handlePanelClicks(ActionEvent event) {
        if (event.getSource()==btnProfile)
        {
            loadProfile();
        }
        else if(event.getSource()==btnFriends)
        {
            loadFriends();
        } else if (event.getSource()==btnMessages) {
            loadMessages();
        } else if (event.getSource()==btnUsers) {
            loadUsers();
        }


    }

    private void loadUsers() {
        FXMLLoader stageLoader=new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/map_socialnetworkvt/views/Users-view.fxml"));
        AnchorPane view= null;
        try {
            view = stageLoader.load();
            borderPane.setCenter(view);
            UserController UserController=stageLoader.getController();
            UserController.setUsersView(serviceUser,serviceFriendships,serviceMessages,user,borderPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMessages() {
        FXMLLoader stageLoader=new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/map_socialnetworkvt/views/Messages-view.fxml"));
        AnchorPane view= null;
        try {
            view = stageLoader.load();
            borderPane.setCenter(view);
            MessagingController UserController=stageLoader.getController();
            UserController.setMessages(serviceUser,serviceFriendships,serviceMessages,user,borderPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }

    private void loadFriends() {
        FXMLLoader stageLoader=new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/map_socialnetworkvt/views/Friends-view.fxml"));
        AnchorPane view= null;
        try {
            view = stageLoader.load();
            borderPane.setCenter(view);
            FriendsController friendsController=stageLoader.getController();
            friendsController.setFriendsWindow(serviceUser,serviceFriendships,serviceMessages,user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        
    }

    @FXML
    private BorderPane borderPane;
    private void loadProfile()  {
        FXMLLoader stageLoader=new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/map_socialnetworkvt/views/Profile-View.fxml"));
        AnchorPane view= null;
        try {
            view = stageLoader.load();
        borderPane.setCenter(view);
        ProfileViewController controller=stageLoader.getController();
        controller.setProfile(serviceUser,serviceFriendships,serviceMessages,user,borderPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setSocialNetwork(UserPagService serviceUsers,
                                 FriendshipsPagService serviceFriendships,
                                 MessagePagService serviceMessages,User user) throws IOException {
        this.serviceUser=serviceUsers;
        this.serviceFriendships=serviceFriendships;
        this.serviceMessages=serviceMessages;
        this.user=user;
        loadProfile();
    }

    @Override
    public void update(SocialNetworkChangeEvent socialNetworkChangeEvent) {

    }

    public void handleClose(ActionEvent event) {

    }
}













