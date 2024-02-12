package com.example.map_socialnetworkvt.Controller;
import java.time.format.DateTimeFormatter;
import com.example.map_socialnetworkvt.Domain.Message.Message;
import com.example.map_socialnetworkvt.Domain.Message.ReplyMessage;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Service.FriendshipsService;
import com.example.map_socialnetworkvt.Service.MessageService;
import com.example.map_socialnetworkvt.Service.UserService;
import com.example.map_socialnetworkvt.ServicePaginated.FriendshipsPagService;
import com.example.map_socialnetworkvt.ServicePaginated.MessagePagService;
import com.example.map_socialnetworkvt.ServicePaginated.UserPagService;
import com.example.map_socialnetworkvt.Utils.Events.Event;
import com.example.map_socialnetworkvt.Utils.Observer.Observer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class MessagingController implements Observer {

    @FXML
    private ScrollPane ScrollPane;

    @FXML
    private ScrollPane ScrollPaneMessages;

    @FXML
    private VBox VBOXMessages;

    @FXML
    private VBox VboxLastMessages;

    @FXML
    private ImageView btnComposeMessage;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnPrevious;

    @FXML
    private Button btnSend;

    @FXML
    private TextArea inputMessage;

    @FXML
    private Label lbl1OfX;

    @FXML
    private TextField lblHowMany;

    @FXML
    private Label loadedUserName;

    @FXML
    private ImageView loadedUserPfp;

    @FXML
    private Label loadedUserUserName;





    private Object selectedMessage=null;
    private UserPagService serviceUser;
    private FriendshipsPagService serviceFriendships;
    private MessagePagService serviceMessages;
    private User user;
    private void addMessageHBox(User userLoad,String message) {
        String username=userLoad.getUsername();
        String imageUrl="/com/example/map_socialnetworkvt/images/userPfps/doggo.jpg";
        HBox messageHBox = new HBox();
        messageHBox.setStyle("-fx-background-color: #ffe3e0; -fx-border-width: 6; -fx-border-color: #ffffff;");

        ImageView imageView = new ImageView(new Image(imageUrl));
        imageView.setFitHeight(56.0);
        imageView.setFitWidth(55.0);

        VBox contentVBox = new VBox();
        Label usernameLabel = new Label(username);
        usernameLabel.setPrefHeight(35.0);
        usernameLabel.setPrefWidth(180.0);

        Label messageLabel = new Label(message);
        messageLabel.setPrefHeight(35.0);
        messageLabel.setPrefWidth(180.0);

        contentVBox.getChildren().addAll(usernameLabel, messageLabel);
        messageHBox.getChildren().addAll(imageView, contentVBox);

        messageHBox.setOnMouseClicked(e->handleLoadConvo(username));


        VboxLastMessages.getChildren().add(messageHBox);
    }


    private void dateLabel(LocalDateTime dateTime)
    {
        String date="Day: "+dateTime.getDayOfMonth()+ " "+dateTime.getMonthValue()+" "+dateTime.getYear();
        Label label=new Label(date);
        label.setPrefWidth(442);
        label.setPrefHeight(26);
        VBOXMessages.getChildren().add(label);
    }
    @FXML
    void handleSend(ActionEvent event) {
        if(selectedMessage==null)
        {
            try
        {
            serviceMessages.addMessage(user.getUsername(),inputMessage.getText(),loadedUserUserName.getText());

        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!!",e.getMessage());
            return;
        }}
        else
        {
            try {

                serviceMessages.sendReply(((Message) selectedMessage).getId(), inputMessage.getText(), user.getUsername());
            }
            catch (RuntimeException e)
                {
                    MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!!!",e.getMessage());
                    return;
                }

            selectedMessage=null;
            VBOXMessages.getChildren().remove(VBOXMessages.getChildren().size()-1);
        }
    }

    private VBox setVboxMes(Object message, Pos alignment, NodeOrientation nodeOrientation) {
        VBox vbox = new VBox();
        vbox.setPrefSize(370, 97);
        vbox.setAlignment(alignment);
        vbox.setNodeOrientation(nodeOrientation);

        String username = ((Message) message).getUserSender().getUsername();

        Label label = new Label(username);
        label.setFont(new Font("Arial Rounded MT Bold", 16));

        Label messageLabel = new Label(((Message) message).getMessage());
        messageLabel.setFont(new Font("System", 10));
        messageLabel.setPrefWidth(337);
        messageLabel.getStyleClass().add(username.equals(user.getUsername()) ? "yourmessage-label" : "message-label");

        if (message instanceof ReplyMessage) {
            Label replyLabel = new Label(((ReplyMessage) message).getReplyfor().getMessage());
            replyLabel.setPrefWidth(337);
            replyLabel.getStyleClass().add(username.equals(user.getUsername()) ? "replyPopUp-label" : "replyMessage-label");

            HBox innerHbox = new HBox();
            innerHbox.getChildren().addAll(label, replyLabel);
            vbox.getChildren().addAll(innerHbox, messageLabel);
        } else {
            vbox.getChildren().addAll(label, messageLabel);
        }

        return vbox;
    }

    private void HBoxMessage(Object mes) {
        HBox messageBox = new HBox();
        messageBox.getStyleClass().add("hbox-styleFriends");

        String formattedDateTime = ((Message) mes).getData().format(DateTimeFormatter.ofPattern("HH:mm"));

        if (user.getUsername().equals(((Message) mes).getUserSender().getUsername())) {
            Pos position = Pos.CENTER;
            messageBox.setAlignment(position);
            messageBox.getChildren().add(setVboxMes(mes, position, NodeOrientation.LEFT_TO_RIGHT));
        } else {
            Pos pos = Pos.CENTER_LEFT;
            NodeOrientation nodeOrientation2 = NodeOrientation.LEFT_TO_RIGHT;
            messageBox.setAlignment(pos);
            messageBox.getChildren().add(setVboxMes(mes, pos, nodeOrientation2));
            ImageView replyImage = new ImageView(new Image("/com/example/map_socialnetworkvt/images/Icons/reply.png"));
            replyImage.setFitWidth(24);
            replyImage.setFitHeight(24);
            replyImage.setOnMouseClicked(o->handleReply(mes));
            messageBox.getChildren().addAll(replyImage, new Label(formattedDateTime));
        }
        VBOXMessages.getChildren().add(messageBox);
    }


    private void handleLoadConvo(String username) {
       List<Object> bubbles=serviceMessages.getConvoObjects(user.getUsername(),username);
       User userLoad=serviceUser.findbyUsername(username);
       loadedUserName.setText(userLoad.getFirstName()+" "+userLoad.getUsername());
       loadedUserUserName.setText(username);
       VBOXMessages.getChildren().clear();
       bubbles.forEach(m->HBoxMessage(m));
        Platform.runLater(() -> {
            ScrollPaneMessages.setVvalue(1.0);
        });




    }

    public void handleComposeMessage(MouseEvent mouseEvent) throws IOException {
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
        messagingController.setMessages(serviceUser,serviceFriendships,serviceMessages,user,null,borderPane);
        dialogStage.show();
    }
    @FXML
    BorderPane borderPane;
    public void setMessages(UserPagService serviceUser, FriendshipsPagService serviceFriendships,
                            MessagePagService servicePagMessages, User user,
                            BorderPane borderPane) {
        this.serviceUser=serviceUser;
        this.serviceFriendships=serviceFriendships;
        this.serviceMessages=servicePagMessages;
        this.user=user;
        serviceUser.addObserver(this);
        serviceMessages.addObserver(this);
        this.setBarLeft();
    }

    private void setBarLeft() {
        VboxLastMessages.getChildren().clear(); // Clear previous content

        // Get the last messages or conversations and add corresponding HBoxes
        Map<User,String> lastMessages = serviceMessages.getConvoEntries(user);// You can adjust the number of last messages to display

        for (User convo:lastMessages.keySet()) {
            String username;
            String lastMessage;

            username=convo.getUsername();
            lastMessage=lastMessages.get(convo);

            addMessageHBox(convo, lastMessage);
        }

    }
@FXML
    public void handleReply(Object mes) {
        Label replyTo=new Label(((Message)mes).getMessage());
        VboxLastMessages.getChildren().add(replyTo);
        selectedMessage=mes;
        Label replymes=new Label(((Message)selectedMessage).getMessage());
        VBOXMessages.getChildren().add(replymes);

    }

    @Override
    public void update(Event event) {
        handleLoadConvo(loadedUserUserName.getText());
        setBarLeft();
    }
}
