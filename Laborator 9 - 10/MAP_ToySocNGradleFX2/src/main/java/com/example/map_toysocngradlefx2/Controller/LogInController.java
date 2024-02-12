package com.example.map_toysocngradlefx2.Controller;

import com.example.map_toysocngradlefx2.Domain.Friendships.Friendship;
import com.example.map_toysocngradlefx2.Domain.User;
import com.example.map_toysocngradlefx2.Service.FriendService;
import com.example.map_toysocngradlefx2.Service.MessageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LogInController {
    MessageAlert messageAlert;
   public FriendService service;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnLogIn;

    @FXML
    private Button btnSignUp;

    @FXML
    private Button btnCancel;
    @FXML
    public ObservableList<User> modelUsers = FXCollections.observableArrayList();
    public ObservableList<Friendship> modelFriendships= FXCollections.observableArrayList();
     Stage primarzStage;
     public MessageService serviceM;
    public void setLogInController(FriendService service, Stage stage, MessageService serviceM ) {
        this.service=service;
        this.primarzStage=stage;
        this.serviceM=serviceM;
    }

    @FXML
    void handleCancel(ActionEvent event) {


    }

    @FXML
    void handleLogIn(ActionEvent event) throws IOException {
        String username=txtUsername.getText();
        String password=txtPassword.getText();
        User userfound=service.findbyUsername(username);
        if(userfound==null)
        {
            MessageAlert.showMessage(primarzStage, Alert.AlertType.ERROR,"Error logging in!","User does not Exist");

        }
        else if(userfound.getPassword().equals(password))
        {
            FXMLLoader stageLoader=new FXMLLoader();
            stageLoader.setLocation(getClass().getResource("/com/example/map_toysocngradlefx2/views/socialnetworkmain-view.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            AnchorPane appLayout=stageLoader.load();

            Scene scene=new Scene(appLayout);
            stage.setScene(scene);

            SocialNetworkMainController controller=stageLoader.getController();
            controller.setSocialNetwork(service,userfound,serviceM);
            stage.show();
        }
        else if(userfound.getPassword()!=password)
        {
            MessageAlert.showMessage(primarzStage, Alert.AlertType.ERROR,"Error logging in!","Incorrect Password");
        }
    }

    @FXML
    void handleSignUp(ActionEvent event) throws IOException {
        FXMLLoader stageLoader=new FXMLLoader();
        URL location=getClass().getResource("/com/example/map_toysocngradlefx2/views/SignUp-view.fxml");
        stageLoader.setLocation(location);
        Stage stage=(Stage) ((Node)event
                .getSource()).getScene().getWindow();

        AnchorPane signUp=stageLoader.load();
        Scene scene=new Scene(signUp);
        stage.setScene(scene);

        SignUpController signUpController=stageLoader.getController();
        signUpController.setSignUp(this.service,this.serviceM);
    }


}
