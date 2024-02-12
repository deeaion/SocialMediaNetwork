package com.example.map_socialnetworkvt.Controller;

import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Service.FriendshipsService;
import com.example.map_socialnetworkvt.Service.MessageService;
import com.example.map_socialnetworkvt.Service.UserService;
import com.example.map_socialnetworkvt.ServicePaginated.FriendshipsPagService;
import com.example.map_socialnetworkvt.ServicePaginated.MessagePagService;
import com.example.map_socialnetworkvt.ServicePaginated.UserPagService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.security.InvalidParameterException;

public class EditUserController {
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnConfirmChanges;

    @FXML
    private Button btnUploadPfp;

    @FXML
    private PasswordField lblConfirmPass;

    @FXML
    private PasswordField lblCurrentPass;

    @FXML
    private TextField lblFirstName;

    @FXML
    private TextField lblLastName;

    @FXML
    private PasswordField lblNewPass;

    @FXML
    private TextField lblUsername;

    @FXML
    void handleChangeUserData(ActionEvent event) {

        String username=lblUsername.getText();

        String first=lblFirstName.getText();
        String second=lblLastName.getText();

        String passwordCurrently=lblCurrentPass.getText();
        String newPass=lblNewPass.getText();
        String confirmPass=lblConfirmPass.getText();
        if(!passwordCurrently.isEmpty()||!newPass.isEmpty()||!confirmPass.isEmpty())
        {
            //inseamna ca vreau sa fac ceva la parola
           try {
            checkPassword(passwordCurrently,newPass,confirmPass);
            serviceUser.updateUser(first,second,username,confirmPass);
           }
           catch (RuntimeException e)
           {
               MessageAlert.showMessage(null, Alert.AlertType.ERROR,"SOMETHING WENT WRONG",e.getMessage());
               return;
           }
            user=new User(first,second,username,confirmPass);

        }
        else
        {
            try {
                serviceUser.updateUser(first,second,username,user.getPassword());
            }
            catch (RuntimeException e)
            {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"SOMETHING WENT WRONG",e.getMessage());
                return;
            }
            user=new User(first,second,username,user.getPassword());

        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"CHANGED","Everything went well!");
        loadFields();




    }

    private void checkPassword(String passwordCurrently, String newPass, String confirmPass) {
        String errors="";
        if(passwordCurrently.isEmpty())
            errors+="YOU HAVE TO ENTER YOUR CURRENT PASSWORD\n";
        else
        {
            if(!passwordCurrently.equals(user.getPassword()))
            {
                errors+="YOU DID NOT ENTER A VALID PASSWORD\n";
            }
            else
            {
                if(!newPass.equals(confirmPass))
                {
                    errors+="YOU DID NOT ENTER THE NEW PASSWORD CORRECTLY!\n";
                }
                else if(newPass.length()<3)
                {
                    errors+="PASS MUST HAVE AT LEAST 3 CHARACTERS\n";
                }
            }

        }
        if(errors.isEmpty())
            return;
        throw new InvalidParameterException(errors);

    }

    @FXML
    void handleExit(ActionEvent event) {


    }
    private UserPagService serviceUser;
    private FriendshipsPagService serviceFriendships;
    private MessagePagService serviceMessages;
    private User user;
    public void setEditUser(UserPagService serviceUsers,
                                 FriendshipsPagService serviceFriendships, MessagePagService serviceMessages,User user) throws IOException {
        this.serviceUser=serviceUsers;
        this.serviceFriendships=serviceFriendships;
        this.serviceMessages=serviceMessages;
        this.user=user;
        loadFields();
    }

    private void loadFields() {
        lblUsername.setText(user.getUsername());
        lblFirstName.setText(user.getFirstName());
        lblLastName.setText(user.getLastName());
        lblUsername.setEditable(false);
        //si o sa mai ai si una de set foto in the future
        //to do

    }
}
