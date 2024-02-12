package com.example.map_socialnetworkvt.Controller;

import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.ServicePaginated.FriendshipsPagService;
import com.example.map_socialnetworkvt.ServicePaginated.MessagePagService;
import com.example.map_socialnetworkvt.ServicePaginated.UserPagService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ComposeMessageController {
    private UserPagService serviceUser;
    private FriendshipsPagService serviceFriendships;
    private MessagePagService serviceMessages;
    private User user;
    private User reciever;
    private BorderPane borderPane;
    public void setMessages(UserPagService serviceUser, FriendshipsPagService serviceFriendships, MessagePagService serviceMessages, User user,
                            User reciever,
                            BorderPane borderPane) {
        this.serviceUser=serviceUser;
        this.serviceFriendships=serviceFriendships;
        this.serviceMessages=serviceMessages;
        this.reciever=reciever;
        this.borderPane=borderPane;
        if(reciever!=null)
        {
            txtRecievers.setText(reciever.getUsername());
        }
        this.user=user;
    }


    @FXML
    private Button btnSend;

    @FXML
    private TextArea txtMessage;

    @FXML
    private TextArea txtRecievers;

    @FXML
    void handleSend(ActionEvent event) {
        try {
            serviceMessages.addMessage(user.getUsername(), txtMessage.getText(),txtRecievers.getText());

        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!!",e.getMessage());
            return;
        }
        openMessagingController();
    }

    private void openMessagingController() {
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
       closeComposeMessageController();

    }
    private void closeComposeMessageController() {
        Stage stage = (Stage) btnSend.getScene().getWindow();
        stage.close();
    }

}
