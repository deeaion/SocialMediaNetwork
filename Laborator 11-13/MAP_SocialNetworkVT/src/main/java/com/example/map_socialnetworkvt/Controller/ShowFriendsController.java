package com.example.map_socialnetworkvt.Controller;

import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Service.FriendshipsService;
import com.example.map_socialnetworkvt.Service.MessageService;
import com.example.map_socialnetworkvt.Service.UserService;
import com.example.map_socialnetworkvt.ServicePaginated.FriendshipsPagService;
import com.example.map_socialnetworkvt.ServicePaginated.MessagePagService;
import com.example.map_socialnetworkvt.ServicePaginated.UserPagService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ShowFriendsController {
    private UserPagService serviceUser;
    private FriendshipsPagService serviceFriendships;
    private MessagePagService serviceMessages;
    private User user;
    private User userWeCheckOn;
    @FXML
    private VBox VBoxList;
    @FXML
    private ImageView btnExit;

    private HBox createFriendHBox(String username,boolean friend) {
        HBox hbox = new HBox();
//        hbox.se
        hbox.getStyleClass().add("hbox-styleFriends"); // Add a style class if needed

        ImageView imageView = new ImageView(new Image("/com/example/map_socialnetworkvt/images/userPfps/cat1.jpg"));
        imageView.setFitHeight(43);
        imageView.setFitWidth(39);

        Label label = new Label("@" + username);
       // label.getStyleClass().add("username-label"); // Add a style class if needed
        if(!username.equals(user.getUsername())) {
            Button addButton = null;
            if (friend) {
                addButton = new Button("UNFRIEND");
            } else {
                addButton = new Button("ADD FRIEND");

            }
            addButton.getStyleClass().add("buttonAddUnfriend"); // Add a style class if needed
            Button finalAddButton = addButton;
            addButton.setOnAction(e -> handleAddOrUnfriendUser(username, finalAddButton));

            hbox.getChildren().addAll(imageView, label, addButton);
        }
        else
        {
            hbox.getChildren().addAll(imageView, label);
        }

        // Add event handlers or other customization as needed

        return hbox;
    }

    private void handleAddOrUnfriendUser(String username,Button addButton) {
        User otherUser=serviceFriendships.findbyUsername(username);
        if(serviceFriendships.getUsersFriends(user).contains(otherUser))
        {
            try
            {
                serviceFriendships.removeFriendship(user.getUsername(),username);
            }
            catch (RuntimeException e)
            {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Canceled Action","Could not unfriend!");
                return;
            }
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Done!","UNFRIENDED");
            addButton.setText("ADD FRIEND");
        }
        else
        {
            try {
                serviceFriendships.addFriendshipRequest(user.getUsername(),username);

            }
            catch (RuntimeException e)
            {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Canceled Action",e.getMessage());
                return;
            }
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Done!","Request was sent!");
//            addButton.setText("PENDING");


        }

    }

    public void setUsers()
    {
        VBoxList.getChildren().clear();
        Iterable<User> users= serviceFriendships.getUsersFriends(userWeCheckOn);

        for(User u:users)
        {
            boolean checkFriend;
            if(serviceFriendships.getUsersFriends(u).contains(user))
            { checkFriend=true;}
            else
            { checkFriend=false;}
           HBox hbox=createFriendHBox(u.getUsername(),checkFriend);
            // Add the HBox to the VBox
            VBoxList.getChildren().add(hbox);
        }
    }
    public void setShowFriends(UserPagService serviceUser, FriendshipsPagService serviceFriendships,
                               MessagePagService serviceMessages, User user
    ,User userWeCheckOn) {
        this.serviceUser=serviceUser;
        this.serviceFriendships=serviceFriendships;
        this.serviceMessages=serviceMessages;
        this.user=user;
        this.userWeCheckOn=userWeCheckOn;
        this.setUsers();

    }

    public void handleExit(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }
}
