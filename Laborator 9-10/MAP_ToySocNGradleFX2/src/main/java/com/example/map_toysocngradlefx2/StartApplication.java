package com.example.map_toysocngradlefx2;

import com.example.map_toysocngradlefx2.Controller.LogInController;
import com.example.map_toysocngradlefx2.Domain.Friendships.Friendship;
import com.example.map_toysocngradlefx2.Domain.Message.Message;
import com.example.map_toysocngradlefx2.Domain.Message.ReplyMessage;
import com.example.map_toysocngradlefx2.Domain.Tuple;
import com.example.map_toysocngradlefx2.Domain.User;
import com.example.map_toysocngradlefx2.Domain.Validators.Validator;
import com.example.map_toysocngradlefx2.Domain.Validators.ValidatorFactory;
import com.example.map_toysocngradlefx2.Domain.Validators.ValidatorStrategy;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Factory.DataBaseRepositoryStrategy;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Factory.DatabaseRepoFactory;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Util.DataBaseAccess;
import com.example.map_toysocngradlefx2.Service.FriendService;
import com.example.map_toysocngradlefx2.Service.MessageService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class StartApplication extends Application {
    private DataBaseAccess data;
    private AbstractDataBaseRepository<Long, User> repositoryUser;
    private AbstractDataBaseRepository<Tuple<Long,Long>, Friendship> repositoryFriendship;
    private AbstractDataBaseRepository<Tuple<Long,Long>, ReplyMessage> repositoryReply;
    private AbstractDataBaseRepository<Long, Message> repositoryMessage;
    public FriendService service;
    public MessageService serviceM;


    @Override
    public void start(Stage primaryStage) throws Exception {
        //here i have my main
        //i am gonna declare everything i need for
        //my db app to work
        ValidatorFactory factory=new ValidatorFactory();
        Validator userValidator=factory.createValidator(ValidatorStrategy.User);
        Validator FriendValidator=factory.createValidator(ValidatorStrategy.Friendship);
        String url="jdbc:postgresql://localhost:5432/socialnetwork2";
        String username="postgres";
        String password="hello";
        this.data=new DataBaseAccess(url,password,username);
        try {
                data.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DatabaseRepoFactory repoFactory=new DatabaseRepoFactory(data);
        this.repositoryUser=repoFactory.createRepositor(DataBaseRepositoryStrategy.users,userValidator);
        this.repositoryFriendship=repoFactory.createRepositor(DataBaseRepositoryStrategy.friendship,FriendValidator);
        this.service=new FriendService(repositoryUser,repositoryFriendship,"DataBase");
        this.repositoryMessage=repoFactory.createRepositor(DataBaseRepositoryStrategy.message,null);
        this.repositoryReply=repoFactory.createRepositor(DataBaseRepositoryStrategy.replymessage,null);
        this.serviceM=new MessageService(repositoryUser,repositoryFriendship,repositoryMessage,repositoryReply);
        this.serviceM.getAllMessage().forEach(System.out::println);
        serviceM.getReplies().forEach(System.out::println);
  //      serviceM.getConvo("jane_smith","john_doe").forEach(System.out::println);
     //   serviceM.addMessage("john_doe","he","jane_smith");
//       serviceM.sendReply(39l,"I got ur message","jane_smith");
        serviceM.getConvo("jane_smith","john_doe").forEach(System.out::println);

        initView(primaryStage);
        primaryStage.setWidth(1000);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader stageLoader=new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/map_toysocngradlefx2/views/LogIn-view.fxml"));
        AnchorPane setLayout=stageLoader.load();
        primaryStage.setTitle("Log In");
        primaryStage.setScene(new Scene(setLayout, Color.POWDERBLUE));
        LogInController logInController=stageLoader.getController();
        logInController.setLogInController(service,primaryStage,serviceM);

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
