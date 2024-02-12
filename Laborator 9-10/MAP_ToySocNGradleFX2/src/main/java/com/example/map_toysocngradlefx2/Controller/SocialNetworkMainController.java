package com.example.map_toysocngradlefx2.Controller;

import com.example.map_toysocngradlefx2.Domain.Entity;
import com.example.map_toysocngradlefx2.Domain.Friendships.Friendship;
import com.example.map_toysocngradlefx2.Domain.Friendships.FriendshipStatus;
import com.example.map_toysocngradlefx2.Domain.Message.Message;
import com.example.map_toysocngradlefx2.Domain.User;
import com.example.map_toysocngradlefx2.Service.FriendService;
import com.example.map_toysocngradlefx2.Service.MessageService;
import com.example.map_toysocngradlefx2.Utils.ActionType;
import com.example.map_toysocngradlefx2.Utils.Events.FriendshipStatusType;
import com.example.map_toysocngradlefx2.Utils.Events.SocialNetworkChangeEvent;
import com.example.map_toysocngradlefx2.Utils.Observer.Observer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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
    private User userLogged;


    //panels
    @FXML
    private StackPane pnlStack;
    //USERS

    @FXML
    public ObservableList<User> modelUsers = FXCollections.observableArrayList();
    public ObservableList<Friendship> modelFriendships= FXCollections.observableArrayList();
    public ObservableList<Message> modelMessage= FXCollections.observableArrayList();
    public ObservableList<String> modelConvo= FXCollections.observableArrayList();
    public ObservableList<String> modelFriends= FXCollections.observableArrayList();
    public ObservableList<String> modelRequests= FXCollections.observableArrayList();

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


    @FXML
    private Button btnUserAdd1;

    @FXML
    private Pane pnlFriends;



    @FXML
    private TableColumn<Friendship,String> tblColStatus;

    @FXML
    private Pane pnlWelcome;

    @FXML
    private Label lblUsernameName;

    @FXML
    private Pane pnlMessages;

    @FXML
    private Button btnHome;


    @FXML
    private Button btnMessages;

    @FXML
    private Button btnFriendships1;
    @FXML
    private ListView<String> listFriends;

    @FXML
    private ListView<String> listRequests;

    @FXML
    private Button btnRemoveFriendship;
    @FXML
    private Button btnDeclineFriendship;
    @FXML
    private Button btnAcceptFriendship;

    public MessageService serviceM;



    @FXML
    private Pane pnlMsgProv;

    @FXML
    private TextField txtRecv;

    @FXML
    private TextArea txtSendMesN;

    @FXML
    private TextField txtIdReplyM;

    @FXML
    private TextArea txtSendMesR;

    @FXML
    private Button btnMessagesUser;

    @FXML
    private Button btnSendText;

    @FXML
    private Button btnSendResponse;

    @FXML
    private TableView<Message> tblMessage;

    @FXML
    private TextField txtUser1;

    @FXML
    private TextField txtUser2;

    @FXML
    private Button btnFindConvo;



    @FXML
    private Button btnMessageProv;

    @FXML
    private TableColumn<Message,Long> tblColIDMes;
    @FXML
    private TableColumn<Message,Long> tblColIDSender;
    @FXML
    private TableColumn<Message,String> tblColMes;
    @FXML
    private TableColumn<Message,String> tblColData;



    public void setSocialNetwork(FriendService service, User user, MessageService serviceM) {
        this.service=service;
        service.addObserver(this);
        this.serviceM=serviceM;
        serviceM.addObserver(this);
        this.userLogged=user;
        lblUsernameName.setText(userLogged.getUsername());
        initModelUsers();
        initModelFriendships();
        InitModelFriends();


    }
    @FXML
    public void initialize()
    {
        initializeTableUsers();
        initializeTableFriendships();
//        Friends
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
       tblColStatus.setCellValueFactory(new PropertyValueFactory<Friendship,String>("status"));
        tblFriendships.setItems(modelFriendships);

    }
    private void setTableMessages()
    {
        tblColIDMes.setCellValueFactory(new PropertyValueFactory<Message,Long>("id"));
        tblColIDSender.setCellValueFactory(message -> {
            User sender = message.getValue().getUserSender();
            long senderId = sender != null ? sender.getId() : 0L;
            return new SimpleObjectProperty<>(senderId);
        });
        tblColMes.setCellValueFactory(new PropertyValueFactory<Message,String>("message"));
        tblColData.setCellValueFactory(new PropertyValueFactory<Message,String>("data"));
        tblMessage.setItems(modelMessage);


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
    private void InitModelFriends()
    {
        Iterable<User> getFriends=service.getFriendshipsOfUsersByStatus(userLogged.getUsername(), FriendshipStatusType.ACCEPTED);
        Iterable<User> getRequests=service.getPendingRequests(userLogged.getUsername());
        List<String> friends=StreamSupport.stream(getFriends.spliterator(),false).map(user -> {return user.getUsername();}).collect(Collectors.toList());
        List<String> requests=StreamSupport.stream(getRequests.spliterator(),false).map(user -> {return user.getUsername();}).collect(Collectors.toList());
        modelFriends.setAll(friends);
        modelRequests.setAll(requests);

    }

    private void initializelistFriends()
    {
        InitModelFriends();
        listFriends.setItems(modelFriends);
        listRequests.setItems(modelRequests);
    }


    @Override
    public void update(SocialNetworkChangeEvent socialNetworkChangeEvent) {
        initModelUsers();
        initModelFriendships();
        InitModelFriends();
        initializelistFriends();
        setTableMessages();
        initializeTableMessages();
     //   initalizeConvo();
    }
    @FXML
    ListView<String> listConvo;
    private void initalizeConvo() {
        listConvo.setItems(modelConvo);

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
        else if(event.getSource()==btnHome)
        {
            lblWindowName.setText("Home");
            pnlWelcome.toFront();
        }
        else if(event.getSource()==btnMessages)
        {
            lblWindowName.setText("Messages");
            pnlMessages.toFront();
        }
        else if(event.getSource()==btnFriendships1)
        {
            lblWindowName.setText("Friends");
            pnlFriends.toFront();
            initializeFriends();
        }
        else if(event.getSource()==btnMessageProv)
        {
            lblWindowName.setText("Message Prov");
            pnlMsgProv.toFront();
        }
    }
    private void initializeTableMessages()
    {
        String username=txtRecv.getText();
        List<Message> messages=serviceM.getMessageForUser(username);
        modelMessage.setAll(messages);
        setTableMessages(

        );


    }
    private void initializeFriends()
    {
        initializelistFriends();

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
        else if(event.getSource()==btnUserAdd1)
        {
            handleAddFriendship();
        }

    }

    private void handleAddFriendship() {
        User wannaAdd=getSelectedUser();
        //cazul in care nu sunt prieteni
        if(wannaAdd==null)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Invalid selection","You did not select anything!");
            return;
        }
        Friendship friendship=service.findFriendship(wannaAdd,userLogged);
        if(friendship==null)
        {
            service.addFriendship(wannaAdd.getUsername(),userLogged.getUsername());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Done","Your Friendship Is pending!");

            return;
        }
        if(friendship.getStatus().equals(FriendshipStatusType.ACCEPTED.name()))
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"INVALID SELECTION","You are already friends with this user!");

        }
        else if(friendship.getStatus().equals(FriendshipStatusType.REJECTED.name()))
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Sorry","This person has rejected your request!");

        }
        else
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"ALREADY SENT","User has yet to have accepted your request!");
        }

    }

    public void handleSceneInputData(User user, ActionType type) {
        try
        {
            URL resourceUrl = getClass().getResource("/com/example/map_toysocngradlefx2/views/InputUserData-view.fxml");
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

    public void handleRemoveFriend(ActionEvent event) {
        String friend=listFriends.getSelectionModel().getSelectedItem();
        if(friend==null)
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"No selection","You did not select anything");
            return;
        }
        else
        {
           service.removeFriendship(friend,userLogged.getUsername());

        }
    }

    public void handleDeclineFriend(ActionEvent event) {
        String friend=listRequests.getSelectionModel().getSelectedItem();
        if(friend==null)
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"No selection","You did not select anything");
            return;
        }
        else
        {
            if(service.modifyFriendship(friend,userLogged.getUsername(), LocalDateTime.now(), FriendshipStatus.REJECTED))
            {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"DONE","You have declined");

            };

        }
    }

    public void handleAcceptFriend(ActionEvent event) {
        String friend=listRequests.getSelectionModel().getSelectedItem();
        if(friend==null)
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"No selection","You did not select anything");
            return;
        }
        else
        {
            if(service.modifyFriendship(friend,userLogged.getUsername(), LocalDateTime.now(), FriendshipStatus.ACCEPTED))
            {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"DONE","You are now Friends");

            };

        }
    }

    public void handleShowMesUser(ActionEvent event) {
        String username=txtRecv.getText();
        User user=service.findbyUsername(username);
        if(user==null)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!!","User does not exist");
        }
        System.out.println("SUNT AICI");
        initializeTableMessages();
    }
    @FXML
    TextField txtToWhom;

    public void handleAddMessage(ActionEvent event) {
        String sender=txtRecv.getText();
        String toWhom=txtToWhom.getText();
        String message=txtSendMesN.getText();
        try
        {serviceM.addMessage(sender,message,toWhom);}
        catch (RuntimeException r)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!!",r.getMessage());
        }

    }

    public void handleSendRespone(ActionEvent event) {
        String sender=txtRecv.getText();
        if(tblMessage.getSelectionModel().getSelectedItem()!=null || !txtIdReplyM.getText().isEmpty()) {
            Long idMes;
            if(tblMessage.getSelectionModel().getSelectedItem()!=null)
            { idMes= tblMessage.getSelectionModel().getSelectedItem().getId();
            txtIdReplyM.setText(idMes.toString());}
            else
                idMes=Long.parseLong(txtIdReplyM.getText());
            String response=txtSendMesR.getText();
            try
            {
                serviceM.sendReply(idMes,response,sender);
            }
            catch (RuntimeException e)
            {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!!",e.getMessage());
            }
        }
        else
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!!","You have not selected or entered any message to reply to!");
        }
    }

    public void handleFindConvo(ActionEvent event) {
        String username1=txtUser1.getText();
        String username2=txtUser2.getText();
        if(!username1.isEmpty()&&!username2.isEmpty())
        { List<String> convo=serviceM.getConvo(username1,username2);
            modelConvo.setAll(convo);
           initalizeConvo();

        }
        else
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!","You have not provided both users");
        }
    }

    public void handleMessageSelect(MouseEvent mouseEvent) {
        txtIdReplyM.setText(tblMessage.getSelectionModel().getSelectedItem().getId().toString());
    }
}
