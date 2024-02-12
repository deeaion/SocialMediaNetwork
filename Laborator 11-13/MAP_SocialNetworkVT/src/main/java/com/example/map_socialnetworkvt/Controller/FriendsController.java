package com.example.map_socialnetworkvt.Controller;

import com.example.map_socialnetworkvt.Domain.Friendships.FriendshipRequest;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Service.FriendshipsService;
import com.example.map_socialnetworkvt.Service.MessageService;
import com.example.map_socialnetworkvt.Service.UserService;
import com.example.map_socialnetworkvt.ServicePaginated.FriendshipsPagService;
import com.example.map_socialnetworkvt.ServicePaginated.MessagePagService;
import com.example.map_socialnetworkvt.ServicePaginated.UserPagService;
import com.example.map_socialnetworkvt.Utils.Events.Event;
import com.example.map_socialnetworkvt.Utils.Observer.Observable;
import com.example.map_socialnetworkvt.Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;
import java.util.stream.StreamSupport;

public class FriendsController implements Observer {
    @FXML
    private VBox VBoxRequests;

    @FXML
    private VBox VboxFriends;

    @FXML
    private Button btnNextFriends;

    @FXML
    private Button btnNextRequests;

    @FXML
    private Button btnPreviousFriends;

    @FXML
    private Button btnPreviousRequests;

    @FXML
    private Label lblNrPageRequest;

    @FXML
    private Label pageNumberFriends;

    @FXML
    void handleNextFriends(ActionEvent event) {
      int page=extractPageNumber(pageNumberFriends.getText());
      page++;
      pageNumberFriends.setText(String.valueOf(page));
      updateFriendsUI();

    }

    @FXML
    void handleNextRequest(ActionEvent event) {
        int page=extractPageNumber(lblNrPageRequest.toString());
        page++;
        lblNrPageRequest.setText(String.valueOf(page));
        updateRequestsUI();
    }

    @FXML
    void handlePrevFriends(ActionEvent event) {
        int page=extractPageNumber(pageNumberFriends.getText());
        page--;
        pageNumberFriends.setText(String.valueOf(page));
        updateFriendsUI();
    }

    @FXML
    void handlePrevRequest(ActionEvent event) {
        int page=extractPageNumber(lblNrPageRequest.toString());
        page--;
        lblNrPageRequest.setText(String.valueOf(page));
        updateRequestsUI();

    }
    int itemsPerPage=8;
    public static int extractPageNumber(String pageString) {
        String numericPart = pageString.replaceAll("\\D+", ""); // Remove non-numeric characters
        return Integer.parseInt(numericPart);
    }
    private void updateFriendsUI() {
        VboxFriends.getChildren().clear(); // Clear existing children

        // Implement logic to fetch friends for the current page
        try
        {
            int page=extractPageNumber(pageNumberFriends.toString());
            List<User> friends = serviceFriendships.getUsersFriendsPerPage(page,itemsPerPage,user);
            modelfriends.setAll(friends.stream().toList());

        // Create HBox elements for each friend and add them to the VBox
        for (User friend : friends) {
            HBox friendHBox = createFriendHBox(friend);
            VboxFriends.getChildren().add(friendHBox);
        }
            btnNextFriends.setDisable(serviceFriendships.getUsersFriendsPerPage(page+1,itemsPerPage,user).isEmpty());
            btnPreviousFriends.setDisable(page-1==0);
        }
        catch (RuntimeException e)
        {
            System.out.println(e.getMessage());
        }
    }
    private void updateRequestsUI() {
        VBoxRequests.getChildren().clear();

        // Implement logic to fetch friends for the current page
        try
        {
            int page=extractPageNumber(lblNrPageRequest.toString());
            Iterable<FriendshipRequest> requests = serviceFriendships.getRequestsForUser(page,itemsPerPage,user);
            modelrequests.setAll(StreamSupport.stream(requests.spliterator(),false).toList());
            // Create HBox elements for each friend and add them to the VBox
            for (FriendshipRequest request : requests) {
                HBox friendHBox = createRequestHBox(request.getUser1());
                VBoxRequests.getChildren().add(friendHBox);
            }
            btnNextRequests.setDisable(((List<FriendshipRequest>)serviceFriendships.getRequestsForUser(page+1,itemsPerPage,user)).isEmpty());
            btnPreviousRequests.setDisable(page-1==0);
        }
        catch (RuntimeException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private HBox createFriendHBox(User friend) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefHeight(60.0);
        hbox.setPrefWidth(131.0);
        hbox.getStyleClass().add("hbox-styleFriends");
        Label usernameLabel = new Label(friend.getUsername());
        usernameLabel.setFont(new Font(19.0));

        Button unfriendButton = new Button("UNFRIEND");
        unfriendButton.setOnAction(e -> handleUnfriendAction(friend));  // Handle unfriend action
        ImageView imageView=new ImageView(new Image("/com/example/map_socialnetworkvt/images/userPfps/cat1.jpg"));
        imageView.setFitWidth(51);
        imageView.setFitHeight(49);
        hbox.getChildren().addAll(imageView, usernameLabel, unfriendButton);

        // Add ImageView, Label, and Button based on friend data
        // Use friend.getUsername(), friend.getProfileImageURL(), etc.

        return hbox;
    }

    private void handleUnfriendAction(User friend) {
        try {
            serviceFriendships.removeFriendship(friend.getUsername(),user.getUsername());

        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!!",e.getMessage());
            return;
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"DONE","You and "+friend.getUsername()+ "are no longer friends!");
    }


    private HBox createRequestHBox(User request) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10.0);
        hbox.getStyleClass().add("hbox-style");

        // Add ImageView, Label, and Buttons based on request data
        // Use request.getUsername(), request.getProfileImageURL(), etc.

        ImageView imageView = new ImageView(new Image("com/example/map_socialnetworkvt/images/userPfps/cat1.jpg"));
        imageView.setFitHeight(49.0);
        imageView.setFitWidth(43.0);

        Label usernameLabel = new Label(request.getUsername());
        usernameLabel.setFont(new Font(13.0));

        Button addButton = new Button("ADD");
        addButton.setOnAction(e -> handleAddAction(request));  // Handle add action

        Button rejectButton = new Button("REJECT");
        rejectButton.setOnAction(e -> handleRejectAction(request));  // Handle reject action

        hbox.getChildren().addAll(imageView, usernameLabel, addButton, rejectButton);

        return hbox;
    }

    private void handleRejectAction(User request) {
        try {
            serviceFriendships.rejectFriendship(request.getUsername(),user.getUsername());

        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!!",e.getMessage());
            return;
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"DONE","You have rejected "+request.getUsername()+ "!");
    }

    private void handleAddAction(User request) {
        try {
            serviceFriendships.acceptFriendship(request.getUsername(),user.getUsername());

        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!!",e.getMessage());
            return;
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"DONE","You have accepted "+request.getUsername()+ " s friendship request!");

    }

    private UserPagService serviceUser;
    private FriendshipsPagService serviceFriendships;
    private MessagePagService serviceMessages;
    private User user;
    ObservableList<User> modelfriends= FXCollections.observableArrayList();
    ObservableList<FriendshipRequest> modelrequests= FXCollections.observableArrayList();
    public void setFriendsWindow(UserPagService serviceUser, FriendshipsPagService serviceFriendships,
                                 MessagePagService serviceMessages, User user) {
        this.serviceUser=serviceUser;
        this.serviceFriendships=serviceFriendships;
        this.serviceMessages=serviceMessages;
        this.user=user;
        serviceUser.addObserver(this);
        serviceFriendships.addObserver(this);
        updateFriendsUI();
        updateRequestsUI();
    }

    @Override
    public void update(Event event) {
        updateRequestsUI();
       updateFriendsUI();
    }
}
