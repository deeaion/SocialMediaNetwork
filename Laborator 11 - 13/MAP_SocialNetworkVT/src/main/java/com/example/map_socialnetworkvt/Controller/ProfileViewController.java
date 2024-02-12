package com.example.map_socialnetworkvt.Controller;

import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Service.FriendshipsService;
import com.example.map_socialnetworkvt.Service.MessageService;
import com.example.map_socialnetworkvt.Service.UserService;
import com.example.map_socialnetworkvt.ServicePaginated.FriendshipsPagService;
import com.example.map_socialnetworkvt.ServicePaginated.MessagePagService;
import com.example.map_socialnetworkvt.ServicePaginated.UserPagService;
import com.example.map_socialnetworkvt.Utils.Events.Event;
import com.example.map_socialnetworkvt.Utils.Observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ProfileViewController implements Observer {
    private UserPagService serviceUser;
    private FriendshipsPagService serviceFriendships;
    private MessagePagService serviceMessages;
    private User user;

    @FXML
    private Button btnDissconect;

    @FXML
    private Button btnEditUser;

    @FXML
    private ImageView imageUser;

    @FXML
    private Label lblFNrFriends;

    @FXML
    private Label lblFullName;

    @FXML
    private Label lblUserName;
    @FXML
    public void initialize()
    {
    }
    @FXML
    BorderPane borderPane;
    public void setProfile(UserPagService serviceUsers,
                           FriendshipsPagService serviceFriendships, MessagePagService serviceMessages, User user,
                           BorderPane borderPane) throws IOException {
        this.serviceUser=serviceUsers;
        this.serviceFriendships=serviceFriendships;
        this.serviceMessages=serviceMessages;
        this.user=user;
        this.borderPane=borderPane;
        serviceUser.addObserver(this);

        setInfo();
    }

    private void setInfo() {
        lblFullName.setText(user.getFirstName()+" "+user.getLastName());
        lblUserName.setText(user.getUsername());
        lblFNrFriends.setText(serviceFriendships.getUsersFriends(user).size() +" FRIENDS");
    }

    public void handleEditUser(ActionEvent event) throws IOException {
        URL resourceUrl = getClass().getResource("/com/example/map_socialnetworkvt/views/EditUser-view.fxml");
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(resourceUrl);
//
        AnchorPane root=(AnchorPane) loader.load();
        Stage dialogStage=new Stage();
        dialogStage.setTitle("Edit Profile");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene=new Scene(root);
        dialogStage.setScene(scene);
        EditUserController editUserControllerr=loader.getController();
        try {
            editUserControllerr.setEditUser(serviceUser,serviceFriendships,serviceMessages,user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dialogStage.show();

    }

    public void handleShowFriends(MouseEvent mouseEvent) {
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

        public void handleLogOut(ActionEvent event) throws IOException {
            FXMLLoader stageLoader=new FXMLLoader();
            stageLoader.setLocation(getClass().getResource("/com/example/map_socialnetworkvt/views/LogIn-view.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            AnchorPane appLayout=stageLoader.load();

            Scene scene=new Scene(appLayout);
            stage.setScene(scene);

            LogInController controller=stageLoader.getController();
            controller.setLogInController(serviceUser,serviceFriendships,serviceMessages,null);
            stage.show();
            return;

    }

    @Override
    public void update(Event event) {
        User newUser=serviceUser.foundById(user.getId());
        user=newUser;
        setInfo();
    }
}
