package com.example.map_socialnetworkvt.Controller;

import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipRequest;
import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipStatus;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.UserFilter;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//import static com.gluonhq.charm.glisten.control.FloatingActionButton.b.e;

public class UserController implements Observer {
    @FXML
    private HBox HBoxUserInfo;

    @FXML
    private ImageView ImagePfpUser;

    @FXML
    private ImageView ImagePfpUserList;

    @FXML
    private Button btnAddUnfriend;

    @FXML
    private Button btnMessage;

    @FXML
    private Label lblFNrFriends;

    @FXML
    private Label lblFullName;

    @FXML
    private Label lblListUsername;

    @FXML
    private TextField lblSearchUser;

    @FXML
    private Label lblUserName;
    @FXML
    private VBox VBoxList;

    private UserPagService serviceUser;
    private FriendshipsPagService serviceFriendships;
    private MessagePagService serviceMessages;
    private User user;
    private User loadedUser;
    @FXML
    public void initialize()
    {

    }

    @FXML
    void handleAddOrUnfriend(Button button) {
        if (button.getText().equals("UNFRIEND")) {
            // Users are already friends, unfriend
            handleUnfriend(button);
        } else if (button.getText().equals("ACCEPT")) {
            // Incoming friend request is pending
            handleAcceptFriendRequest(button);
        } else if (button.getText().equals("ADD FRIEND")) {
            // No existing relationship, send friend request
            handleSendFriendRequest(button);
        }
    }

    private void handleSendFriendRequest(Button button) {
        try {
            serviceFriendships.addFriendshipRequest(user.getUsername(),loadedUser.getUsername());

        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Canceled Action",e.getMessage());
            return;
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Done!","Request was sent!");
        button.setText("PENDING");

    }

    private void handleAcceptFriendRequest(Button button) {
        try {
            serviceFriendships.acceptFriendship(loadedUser.getUsername(),user.getUsername());

        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!!",e.getMessage());
            return;
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"DONE","You have accepted "+loadedUser.getUsername()+ " s friendship request!");


    }

    private void handleUnfriend(Button button) {
        try
        {
            serviceFriendships.removeFriendship(user.getUsername(),loadedUser.getUsername());
        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Canceled Action","Could not unfriend!");
            return;
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Done!","UNFRIENDED");
        button.setText("ADD FRIEND");
    }

    @FXML
    Rectangle RectBehind;
    @FXML
    HBox HboxProfile;
    @FXML
    void handleLoadProfile(User u) {
        loadedUser=u;
        if(!RectBehind.isVisible()||!HboxProfile.isVisible()||!btnAddUnfriend.isVisible())
        {
            RectBehind.setVisible(true);
            HboxProfile.setVisible(true);
            btnAddUnfriend.setVisible(true);
        }
        lblFullName.setText(u.getFirstName()+" "+u.getLastName());
        lblUserName.setText(u.getUsername());
        lblFNrFriends.setText(serviceFriendships.getUsersFriends(u).size() +" FRIENDS");
        if(!u.equals(user))
            {
                if(serviceFriendships.findFriendship(u,user)!=null)
                    {
                        btnAddUnfriend.setText("UNFRIEND");
                    }
                else
                    {
                        FriendshipRequest foundRequest=serviceFriendships.findFriendshipRequest(user,u);
                        if(foundRequest!=null&&foundRequest.getStatus().equals(FriendshipStatus.PENDING))
                        {
                            btnAddUnfriend.setText("PENDING");
                        }
                        foundRequest=serviceFriendships.findFriendshipRequest(u,user);
                        if(serviceFriendships.findFriendshipRequest(u,user)!=null)
                        {
                             btnAddUnfriend.setText("ACCEPT");
                        }
                        btnAddUnfriend.setText("ADD FRIEND");

                    }
                    btnAddUnfriend.setOnAction(b->handleAddOrUnfriend(btnAddUnfriend));}
        else
        {
            btnAddUnfriend.setVisible(false);
        }

    }

    @FXML
    void handleMessage(ActionEvent event) throws IOException {
        URL resourceUrl = getClass().getResource("/com/example/map_socialnetworkvt/views/ComposeMessage-view.fxml");
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(resourceUrl);
//
        AnchorPane root=(AnchorPane) loader.load();
        Stage dialogStage=new Stage();
        dialogStage.setTitle("Compose Message");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene=new Scene(root);
        dialogStage.setScene(scene);
        ComposeMessageController messagingController=loader.getController();
        messagingController.setMessages(serviceUser,serviceFriendships,serviceMessages,user,loadedUser,borderPane);
        dialogStage.show();

    }

    @FXML
    void handleShowFriends(MouseEvent event) throws IOException {
        URL resourceUrl = getClass().getResource("/com/example/map_socialnetworkvt/views/ShowFriends-view.fxml");
//            System.out.println("Resource URL: " + resourceUrl);
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(resourceUrl);
//
            AnchorPane root=(AnchorPane) loader.load();
//            //create stage dialog
//
            Stage dialogStage=new Stage();
            dialogStage.setTitle("Friends");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene=new Scene(root);
            dialogStage.setScene(scene);
            ShowFriendsController showFriendsController=loader.getController();
            showFriendsController.setShowFriends(serviceUser,serviceFriendships,serviceMessages,user,loadedUser);
            dialogStage.show();




    }
    @FXML
    private Button btnNext;

    @FXML
    private Button btnPrev;
    @FXML
    private Label lblPageNumber;
    @FXML
    private TextField txtItemsPerPage;
    @FXML
    void handleNextPage(ActionEvent event) {
        int next= Integer.parseInt(lblPageNumber.getText())+1;
        lblPageNumber.setText(String.valueOf(next));
        setUsers();


    }

    @FXML
    void handlePrevPage(ActionEvent event) {
        int prev= Integer.parseInt(lblPageNumber.getText())-1;
        lblPageNumber.setText(String.valueOf(prev));
        setUsers();
    }
    public void setUsers()
    {
        try
        {
        VBoxList.getChildren().clear();
        int page= Integer.parseInt(lblPageNumber.getText());
        int objPerPage= Integer.parseInt(txtItemsPerPage.getText());
        UserFilter filter=new UserFilter();
        filter.setFiltering(lblSearchUser.getText());
        filter.setOperator("or");
        Iterable<User> users= serviceUser.getPaginatedUsers(page,objPerPage,filter);
        for(User u:users)
        {
            HBox hbox=new HBox();
            hbox.getStyleClass().add("hbox-style");
            ImageView imageView=new ImageView(new Image("/com/example/map_socialnetworkvt/images/foxy.jpg"));
            imageView.setFitHeight(53.0);
            imageView.setFitWidth(51.0);
            Label usernameLabel = new Label(u.getUsername());
//            usernameLabel.getStyleClass().add("your-label-style"); // Add your CSS style class if needed
            hbox.setOnMouseClicked(o->handleLoadProfile(u));
            hbox.getChildren().addAll(imageView, usernameLabel);

        // Add the HBox to the VBox
            VBoxList.getChildren().add(hbox);
        }
        Iterable<User> next=serviceUser.getPaginatedUsers(page+1,objPerPage,filter);
        btnNext.setDisable(((List<User>) serviceUser.getPaginatedUsers(page + 1, objPerPage, filter)).isEmpty());
        btnPrev.setDisable(page-1==0);


        }

        catch (RuntimeException e)
        {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    BorderPane borderPane;
    public void setUsersView(UserPagService serviceUser, FriendshipsPagService serviceFriendships, MessagePagService serviceMessages, User user, BorderPane borderPane) {
        this.serviceUser=serviceUser;
        this.serviceFriendships=serviceFriendships;
        this.serviceMessages=serviceMessages;
        this.user=user;
        lblSearchUser.textProperty().addListener(o->setUsers());
        txtItemsPerPage.textProperty().addListener(o->setUsers());
        btnNext.setDisable(true);
        btnNext.setDisable(true);
        this.borderPane=borderPane;
        setUsers();
    }



    @Override
    public void update(Event event) {
      setUsers();

    }
}
