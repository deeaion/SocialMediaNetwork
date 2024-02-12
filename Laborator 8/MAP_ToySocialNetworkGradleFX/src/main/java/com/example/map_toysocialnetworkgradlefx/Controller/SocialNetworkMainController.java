package com.example.map_toysocialnetworkgradlefx.Controller;

import com.example.map_toysocialnetworkgradlefx.Domain.Entity;
import com.example.map_toysocialnetworkgradlefx.Domain.Friendships.Friendship;
import com.example.map_toysocialnetworkgradlefx.Domain.User;
import com.example.map_toysocialnetworkgradlefx.Service.FriendService;
import com.example.map_toysocialnetworkgradlefx.Utils.ActionType;
import com.example.map_toysocialnetworkgradlefx.Utils.Events.SocialNetworkChangeEvent;
import com.example.map_toysocialnetworkgradlefx.Utils.Observer.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SocialNetworkMainController implements Observer<SocialNetworkChangeEvent> {
    FriendService service;
    //MAIN BUTTONS
    @FXML
    private Label lblWindowName;
    @FXML
    private Button btnUsers;

    @FXML
    private Button btnFriendships;


    //panels
    @FXML
    private StackPane pnlStack;
    //USERS

    @FXML
    public ObservableList<User> modelUsers = FXCollections.observableArrayList();
    public ObservableList<Friendship> modelFriendships= FXCollections.observableArrayList();
    @FXML
    private Pane pnlUsers;

    @FXML
    private TableView<User> tblUsers;

    @FXML
    private TableColumn<User, Long> tblUserID;

    @FXML
    private TableColumn<User, String> tblUserFName;

    @FXML
    private TableColumn<User, String> tblLName;

    @FXML
    private TableColumn<User, String> tblUserUserName;

    @FXML
    private TextField txtFieldUserSearch;

    @FXML
    private Button btnSearchUser;

    @FXML
    private Button btnUserAdd;

    @FXML
    private Button btnUserDelete;

    @FXML
    private Button btnUserModify;


    //panel Friendship

    @FXML
    private GridPane pnlFriendships;

    @FXML
    private TextField txtFieldFrUsername;
    @FXML
    private TableColumn<Friendship,String> tblColUserName2;

    @FXML
    private TextField txtFieldyear;

    @FXML
    private TextField txtFieldMonth;

    @FXML
    private Button btnSearchFriends;

    @FXML
    private TableView<Friendship> tblFriendships;

    @FXML
    private TableColumn<Friendship, String> tblColUserName1;


    @FXML
    private TableColumn<Friendship, String> tblColDate;


    public void setSocialNetworkService(FriendService service) {
        this.service=service;
        service.addObserver(this);
        initModelUsers();
    //    initModelFriendships();
    }
    @FXML
    public void initialize()
    {
        initializeTableUsers();
        initializeTableFriendships();
    }
    private void initializeTableUsers()
    {
        tblUserID.setCellValueFactory(new PropertyValueFactory<User,Long>("id"));
        tblUserFName.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        tblLName.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
        tblUserUserName.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        tblUsers.setItems(modelUsers);

    }
    private void initializeTableFriendships()
    {
        tblColUserName1.setCellValueFactory(cellData1 -> new SimpleStringProperty((cellData1.getValue()).getUser1().getUsername()));
//        tblColUserName2.setCellValueFactory(cellData1 -> new SimpleStringProperty(((Friendship)cellData1.getValue()).getUser1().getUsername()));
       tblColUserName2.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getUser2().getUsername()));
       tblColDate.setCellValueFactory(new PropertyValueFactory<Friendship,String>("date"));
        tblFriendships.setItems(modelFriendships);

    }

    private void initModelUsers() {
        Iterable<User> users=service.getAllUsers();
        List<User> userList= StreamSupport.stream(users.spliterator(),false).collect(Collectors.toList());
        modelUsers.setAll(userList);

    }
    private void initModelFriendships() {
        Iterable<Friendship> friendships=service.getAllFriendships();
        List<Friendship> friendshipListi= StreamSupport.stream(friendships.spliterator(),false).collect(Collectors.toList());
       modelFriendships.setAll(friendshipListi);

    }


    @Override
    public void update(SocialNetworkChangeEvent socialNetworkChangeEvent) {
        initModelUsers();
        initModelFriendships();
    }
    @FXML
    private void handlePanelClicks(ActionEvent event)
    {

        if(event.getSource()==btnUsers)
        {
            lblWindowName.setText("Users");
            pnlUsers.toFront();
        }
        else if(event.getSource()==btnFriendships)
        {
            lblWindowName.setText("Friendships");
            pnlFriendships.toFront();
        }
    }
    private User getSelectedUser()
    {
        User selected=(User) tblUsers.getSelectionModel().getSelectedItem();
        return selected;
    }
    @FXML
    private void handleUserPanelClicks(ActionEvent event)
    {

        if(event.getSource()==btnUserAdd)
        {
            handleSceneInputData(null,ActionType.SAVE);
        }
        else if(event.getSource()==btnUserModify)
        {
            handleSceneInputData(getSelectedUser(),ActionType.MODIFY);
        }
        else if(event.getSource()==btnUserDelete)
        {
            handleDeleteUser(getSelectedUser());
        }
        else if(event.getSource()==btnSearchUser)
        {
            handleSearchUser();
        }
    }

    public void handleSceneInputData(User user, ActionType type) {
        try
        {
            URL resourceUrl = getClass().getResource("/com/example/map_toysocialnetworkgradlefx/views/InputUserData-view.fxml");
            System.out.println("Resource URL: " + resourceUrl);
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(resourceUrl);

            AnchorPane root=(AnchorPane) loader.load();
            //create stage dialog

            Stage dialogStage=new Stage();
            dialogStage.setTitle("Input Data");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene=new Scene(root);
            dialogStage.setScene(scene);
            InputUserDataController inputUserDataController=loader.getController();
            inputUserDataController.setService(service,dialogStage,user,type);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteUser(User selected) {

            if(selected!=null)

            {Entity deleted=service.removeUser(selected.getUsername());
            if(null!=deleted)
            {
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Removal-- Information","Your item was removed!");

            }}
            else MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Removal -- Information","You have not selected any user!");


    }
    private void handleSearchUser() {
        String idS=txtFieldUserSearch.getText();
        if(idS.isEmpty())
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Search -- Information","You have not entered any ID!");
            return;
        }
        Long ID=Long.parseLong(idS);
        User found=service.foundById(ID);
        if(found==null)
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Search -- Information","User does not exist!");

        }
        else
        {
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Search -- Succesfull","User exists with these info: "+found.toString());

        }


    }

}
