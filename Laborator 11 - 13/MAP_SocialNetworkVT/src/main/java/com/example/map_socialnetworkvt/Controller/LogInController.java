package com.example.map_socialnetworkvt.Controller;

import com.example.map_socialnetworkvt.Domain.Friendships.Friendship;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.ServicePaginated.FriendshipsPagService;
import com.example.map_socialnetworkvt.ServicePaginated.MessagePagService;
import com.example.map_socialnetworkvt.ServicePaginated.UserPagService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.net.URL;

public class LogInController {
    MessageAlert messageAlert;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogIn;
    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnCancel;
    @FXML
    public ObservableList<User> modelUsers = FXCollections.observableArrayList();
    public ObservableList<Friendship> modelFriendships= FXCollections.observableArrayList();
     Stage primaryStage;
//    private UserService serviceUser;
//    private FriendshipsService serviceFriendships;
//    private MessageService serviceMessages;
    private User user;
    private UserPagService serviceUser;
    private FriendshipsPagService serviceFriendships;
    private MessagePagService serviceMessages;

    public void setLogInController(UserPagService serviceUsers, FriendshipsPagService serviceFriendships,
                                   MessagePagService serviceMessages,
                                   Stage stage) {
        this.serviceUser=serviceUsers;
        this.serviceFriendships=serviceFriendships;
        this.serviceMessages=serviceMessages;
        this.primaryStage=stage;
    }

    @FXML
    void handleCancel(ActionEvent event) {


    }
    private void openMainWindow(User user) throws IOException {
        String fxmlPath;
        if (user.getUsername().equals("admin")) {
            fxmlPath = "/com/example/map_socialnetworkvt/views/Admin-view.fxml";
        } else {
            fxmlPath = "/com/example/map_socialnetworkvt/views/socialnetworkmain-view.fxml";
        }

        FXMLLoader stageLoader = new FXMLLoader();
        stageLoader.setLocation(getClass().getResource(fxmlPath));
        Stage stage = new Stage();  // Create a new stage for the new window

        AnchorPane appLayout = stageLoader.load();
        Scene scene = new Scene(appLayout);
        stage.setScene(scene);

        if (user.getUsername().equals("admin")) {
            AdminController controller = stageLoader.getController();
            controller.setAdminController(serviceUser, serviceFriendships, serviceMessages, user);
        } else {
            SocialNetworkMainController controller = stageLoader.getController();
            controller.setSocialNetwork(serviceUser, serviceFriendships, serviceMessages, user);
        }

        stage.show();
    }

    @FXML
    void handleLogIn(ActionEvent event) throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        User userfound = serviceUser.findbyUsername(username);

        if (userfound == null) {
            MessageAlert.showMessage(primaryStage, Alert.AlertType.ERROR, "Error logging in!", "User does not Exist");
        } else if (serviceUser.verifyPassword(password,userfound)) {
            openMainWindow(userfound);
        } else {
            MessageAlert.showMessage(primaryStage, Alert.AlertType.ERROR, "Error logging in!", "Incorrect Password");
        }
    }

    @FXML
    void handleSignUp(ActionEvent event) throws IOException {
        FXMLLoader stageLoader=new FXMLLoader();
        URL location=getClass().getResource("/com/example/map_socialnetworkvt/views/SignUp-view.fxml");
        stageLoader.setLocation(location);
        Stage stage=(Stage) ((Node)event
                .getSource()).getScene().getWindow();

        AnchorPane signUp=stageLoader.load();
        Scene scene=new Scene(signUp);
        stage.setScene(scene);

        SignUpController signUpController=stageLoader.getController();
        signUpController.setSignUpController(this.serviceUser,this.serviceFriendships,this.serviceMessages);
    }


}
