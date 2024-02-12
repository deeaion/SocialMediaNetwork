package com.example.map_socialnetworkvt.Controller;

import com.example.map_socialnetworkvt.Domain.Entity;
import com.example.map_socialnetworkvt.Domain.Friendships.Friendship;
import com.example.map_socialnetworkvt.Domain.Message.Message;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.FriendshipFilter;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.UserFilter;
import com.example.map_socialnetworkvt.ServicePaginated.FriendshipsPagService;
import com.example.map_socialnetworkvt.ServicePaginated.MessagePagService;
import com.example.map_socialnetworkvt.ServicePaginated.UserPagService;
import com.example.map_socialnetworkvt.Utils.ActionType;
import com.example.map_socialnetworkvt.Utils.Events.SocialNetworkChangeEvent;
import com.example.map_socialnetworkvt.Utils.Observer.Observer;
import javafx.beans.InvalidationListener;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AdminController implements Observer<SocialNetworkChangeEvent> {
    @FXML
    private AnchorPane FriendshipWindow;
    private UserPagService serviceUser;
    private FriendshipsPagService serviceFriendships;
    private MessagePagService serviceMessages;
    private User user;
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
    public ObservableList<Message> modelMessage= FXCollections.observableArrayList();
    public ObservableList<String> modelConvo= FXCollections.observableArrayList();
    @FXML
    public TextField txtFromWhomTheRequestIsSent;
//users
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

    ///welcome window
    @FXML
    private Pane pnlWelcome;
    @FXML
    private Button btnHome;
    //control messages
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
    @FXML
    private Label lblPage;
    @FXML
    private TextField txtFieldItems;



    public void setAdminController(UserPagService serviceUsers,
                                   FriendshipsPagService serviceFriendships, MessagePagService serviceMessages,
                                   User user){
        this.serviceUser=serviceUsers;
        this.serviceFriendships=serviceFriendships;
        this.serviceMessages=serviceMessages;
        this.user=user;
        btnPrevious.setDisable(true);
        btnNext.setDisable(true);
        txtFieldItems.textProperty().addListener(a->initModelUsers());
        lblNrFriendsPage.textProperty().addListener(a->initModelFriendships());
        initModelFriendships();
        initModelUsers();

        initialize();
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
    @FXML
   Button btnNext;
    private void initModelUsers() {
        int page= Integer.parseInt(lblPage.getText());
        try {
            int objectsPerPage = Integer.parseInt(txtFieldItems.getText());
            UserFilter filter=new UserFilter();
            Iterable<User> users=serviceUser.getPaginatedUsers(page,objectsPerPage,filter);
            List<User> userList= StreamSupport.stream(users.spliterator(),false).collect(Collectors.toList());
            List<User> next= (List<User>) serviceUser.getPaginatedUsers(page + 1, objectsPerPage, filter);
            btnNext.setDisable(next.isEmpty());
            btnPrevious.setDisable(page - 1 == 0);
            modelUsers.setAll(userList);
        }
        catch (RuntimeException e)
        {
            System.out.println(e.getMessage());
        }



    }
    @FXML
    Button btnPrevFriend;
    @FXML
    Button btnNextFriend;
    @FXML
    Label lblFriendshipPage;
    @FXML
    TextField lblNrFriendsPage;
    private void initModelFriendships() {
        try
        {
            int page= Integer.parseInt(lblFriendshipPage.getText());
            int objectsPerPage = Integer.parseInt(lblNrFriendsPage.getText());
            FriendshipFilter filter=new FriendshipFilter();
            Iterable<Friendship> friendships=serviceFriendships.getFriendshipsPaginated(page,objectsPerPage,filter);
            List<Friendship> next= (List<Friendship>) serviceFriendships.getFriendshipsPaginated(page+1,objectsPerPage,filter);
            btnNextFriend.setDisable(next.isEmpty());
            btnPrevFriend.setDisable(page - 1 == 0);
           modelFriendships.setAll((Collection<? extends Friendship>) friendships);}
        catch (RuntimeException e)
        {
            System.out.println(e.getMessage());
        }

    }
    public void handleprevFriend(ActionEvent event) {
        int page=Integer.parseInt(lblFriendshipPage.getText())-1;
        lblFriendshipPage.setText(String.valueOf(page));
        initModelFriendships();
    }
    public void handleNextFriend(ActionEvent event) {
        int page=Integer.parseInt(lblFriendshipPage.getText())+1;
        lblFriendshipPage.setText(String.valueOf(page));

        initModelFriendships();
    }




    @Override
    public void update(SocialNetworkChangeEvent socialNetworkChangeEvent) {
        initModelUsers();
        initModelFriendships();
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
            FriendshipWindow.toFront();
        }
        else if(event.getSource()==btnHome)
        {
            lblWindowName.setText("Home");
            pnlWelcome.toFront();
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
        List<Message> messages=serviceMessages.getMessageForUser(username);
        modelMessage.setAll(messages);
        setTableMessages(

        );


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
            handleSceneInputData(null, ActionType.SAVE);
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
        User userLogged=serviceUser.findbyUsername(txtFromWhomTheRequestIsSent.getText());
        if(wannaAdd==null)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Invalid username","You did not enter a valid username!");
            return;
        }
        Friendship friendship=serviceFriendships.findFriendship(wannaAdd,userLogged);
        if(friendship==null)
        {
            serviceFriendships.addFriendship(wannaAdd.getUsername(),userLogged.getUsername());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Done","Your Friendship Is pending!");

            return;
        }
        else
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"INVALID SELECTION","You are already friends with this user!");

        }

    }

    public void handleSceneInputData(User userL, ActionType type) {
        try
        {
            URL resourceUrl = getClass().getResource("/com/example/map_socialnetworkvt/views/InputUserData-view.fxml");
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
            inputUserDataController.setService(serviceUser,dialogStage,userL,type);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteUser(User selected) {

        if(selected!=null)

        {
            Entity deleted=serviceUser.removeUser(selected.getUsername());
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
        User found=serviceUser.foundById(ID);
        if(found==null)
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Search -- Information","User does not exist!");

        }
        else
        {
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Search -- Succesfull","User exists with these info: "+found.toString());

        }


    }
    @FXML
    private ListView<String> listFriends;
    public void handleRemoveFriend(ActionEvent event) {
        String friend=listFriends.getSelectionModel().getSelectedItem();
        if(friend==null)
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"No selection","You did not select anything");
            return;
        }
        else
        {
            serviceFriendships.removeFriendship(friend,user.getUsername());

        }
    }
    @FXML
    private ListView<String> listRequests;

    public void handleDeclineFriend(ActionEvent event) {
        String friend=listRequests.getSelectionModel().getSelectedItem();
        if(friend==null)
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"No selection","You did not select anything");
            return;
        }
        else
        {
            try
            {
                serviceFriendships.rejectFriendship(friend,user.getUsername());
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"DONE","You have declined");

            }
            catch (RuntimeException e)
            {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"DONE",e.getMessage());

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
           try {
               serviceFriendships.acceptFriendship(friend, user.getUsername());
               MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"DONE","You are now Friends");
           }
           catch (RuntimeException e)
           {
               MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"DONE",e.getMessage());

           }

        }
    }

    public void handleShowMesUser(ActionEvent event) {
        String username=txtRecv.getText();
        User userL=serviceUser.findbyUsername(username);
        if(userL==null)
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
        {serviceMessages.addMessage(sender,message,toWhom);}
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
                serviceMessages.sendReply(idMes,response,sender);
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
        { List<String> convo=serviceMessages.getConvo(username1,username2);
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

    public void handlepreviousPage(ActionEvent event) {

        int previousPage=Integer.parseInt(lblPage.getText())-1;
        if(previousPage==0)
        {
          //  btnPrevious.setDisable(true);
            return;
        }
        lblPage.setText(String.valueOf(previousPage));
        initModelUsers();


    }
@FXML
private Button btnPrevious;
    public void handleNextPage(ActionEvent event) {
        lblPage.setText(String.valueOf(Integer.parseInt(lblPage.getText())+1));
        initModelUsers();
       // btnPrevious.setDisable(false);

    }


}
