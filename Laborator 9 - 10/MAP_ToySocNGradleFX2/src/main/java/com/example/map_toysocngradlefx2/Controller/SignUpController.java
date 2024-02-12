package com.example.map_toysocngradlefx2.Controller;

import com.example.map_toysocngradlefx2.Domain.User;
import com.example.map_toysocngradlefx2.Service.FriendService;
import com.example.map_toysocngradlefx2.Service.MessageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    FriendService service;
    MessageService serviceM;

    @FXML
    private TextField txtFName;

    @FXML
    private TextField txtLName;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPas;

    @FXML
    private PasswordField txtConfPas;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnCancel;
    @FXML
    Stage thisStage;

    @FXML
    void handleCancel(ActionEvent event) throws IOException {
        FXMLLoader stageLoader=new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/map_toysocngradlefx2/views/LogIn-view.fxml"));
        Stage stageLog=(Stage) ((Node)event.getSource()).getScene().getWindow();
        AnchorPane logIn=stageLoader.load();
        Scene newScene=new Scene(logIn);
        stageLog.setScene(newScene);
        LogInController controller=stageLoader.getController();
        controller.setLogInController(service,stageLog,serviceM);

    }

    @FXML
    void handleSave(ActionEvent event) throws IOException {
        String firstName=txtFName.getText();
        String lastName=txtLName.getText();
        String userName=txtUsername.getText();
        String password=txtPas.getText();
        String confPassword=txtConfPas.getText();
        if(!password.equals(confPassword))
        {
            MessageAlert.showMessage(null,Alert.AlertType.ERROR,"!!!Creation --- Unsuccessful ","Password is incorrect!");

        }
        User u=new User(firstName,lastName,userName,password);

        try
        {
            boolean done=this.service.addUser(u.getFirstName(),u.getLastName(),u.getUsername(),u.getPassword());
            if(done)
            {
                FXMLLoader stageLoader=new FXMLLoader();
                stageLoader.setLocation(getClass().getResource("/com/example/map_toysocngradlefx2/views/LogIn-view.fxml"));
                Stage stageLog=(Stage) ((Node)event.getSource()).getScene().getWindow();
                AnchorPane logIn=stageLoader.load();
                Scene newScene=new Scene(logIn);
                stageLog.setScene(newScene);
                LogInController controller=stageLoader.getController();
                controller.setLogInController(service,stageLog,serviceM);
            }
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Save --- result","User was saved succesfully");
        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null,Alert.AlertType.ERROR,"!!!Save --- Unsuccessful",e.getMessage());
        }

    }

    public void setSignUp(FriendService service,MessageService m) {
        this.service=service;
        this.serviceM=m;
    }
}
