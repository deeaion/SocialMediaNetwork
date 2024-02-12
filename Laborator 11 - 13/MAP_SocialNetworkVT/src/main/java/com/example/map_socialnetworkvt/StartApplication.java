package com.example.map_socialnetworkvt;

//import com.example.map_socialnetworkvt.Controller.LogInController;

import com.example.map_socialnetworkvt.Controller.LogInController;
import com.example.map_socialnetworkvt.Domain.Friendships.Friendship;
import com.example.map_socialnetworkvt.Domain.Message.Message;
import com.example.map_socialnetworkvt.Domain.Message.ReplyMessage;
import com.example.map_socialnetworkvt.Domain.Tuple;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Factory.DataBasePaginatedFactory;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.Factory.DatabasePaginatedRepoFactory;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.FilterMethod;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.UserFilter;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FriendshipDBRepo.FriendshipPaginatedDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FriendshipDBRepo.FriendshipPaginatedRequestDatabase;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.MessageRepo.MessagePaginatedDatabaseRespository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.MessageRepo.ReplyMessagePaginatedDatabaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.PageableImplementation;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.UserPaginatedDatabaseRepository;
import com.example.map_socialnetworkvt.Service.FriendshipsService;
import com.example.map_socialnetworkvt.Service.MessageService;
import com.example.map_socialnetworkvt.ServicePaginated.FriendshipsPagService;
import com.example.map_socialnetworkvt.ServicePaginated.MessagePagService;
import com.example.map_socialnetworkvt.ServicePaginated.UserPagService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.example.map_socialnetworkvt.Domain.User;
import com.example.map_socialnetworkvt.Domain.Validators.Validator;
import com.example.map_socialnetworkvt.Domain.Validators.ValidatorFactory;
import com.example.map_socialnetworkvt.Domain.Validators.ValidatorStrategy;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Factory.DataBaseRepositoryStrategy;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Factory.DatabaseRepoFactory;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util.DataBaseAccess;
import com.example.map_socialnetworkvt.Service.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class StartApplication extends Application {
    private DataBaseAccess data;
    private UserPaginatedDatabaseRepository repositoryUser;
    private FriendshipPaginatedDatabase repositoryFriendship;
    private FriendshipPaginatedRequestDatabase repositoryRequests;
    private ReplyMessagePaginatedDatabaseRepository repositoryReply;
    private MessagePaginatedDatabaseRespository repositoryMessage;
//    public UserService serviceUsers;
//    public FriendshipsService serviceFriends;
//    public MessageService serviceMessages;
    public UserPagService serviceUsers;
    public FriendshipsPagService serviceFriends;
    public MessagePagService serviceMessages;


    @Override
    public void start(Stage primaryStage) throws Exception {
        //here i have my main
        //i am gonna declare everything i need for
        //my db app to work
        ValidatorFactory factory=new ValidatorFactory();
        Validator userValidator=factory.createValidator(ValidatorStrategy.User);
        Validator FriendValidator=factory.createValidator(ValidatorStrategy.Friendship);
        String url="jdbc:postgresql://localhost:5432/socialnetwork3";
        String username="postgres";
        String password="hello";
        this.data=new DataBaseAccess(url,password,username);
        try {
                data.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        DatabaseRepoFactory repoFactory=new DatabaseRepoFactory(data);
//        this.repositoryUser=repoFactory.createRepositor(DataBaseRepositoryStrategy.users,userValidator);
//        this.repositoryFriendship=repoFactory.createRepositor(DataBaseRepositoryStrategy.friendship,FriendValidator);
//        this.repositoryRequests=repoFactory.createRepositor(DataBaseRepositoryStrategy.request,null);
//        this.repositoryMessage=repoFactory.createRepositor(DataBaseRepositoryStrategy.message,null);
//        this.repositoryReply=repoFactory.createRepositor(DataBaseRepositoryStrategy.replymessage,null);
        DatabasePaginatedRepoFactory repoFactory=new DatabasePaginatedRepoFactory(data);
        this.repositoryUser= (UserPaginatedDatabaseRepository) repoFactory.createRepositor(DataBaseRepositoryStrategy.users,userValidator);
        this.repositoryFriendship= (FriendshipPaginatedDatabase) repoFactory.createRepositor(DataBaseRepositoryStrategy.friendship,FriendValidator);
        this.repositoryRequests= (FriendshipPaginatedRequestDatabase) repoFactory.createRepositor(DataBaseRepositoryStrategy.request,null);
        this.repositoryMessage= (MessagePaginatedDatabaseRespository) repoFactory.createRepositor(DataBaseRepositoryStrategy.message,null);
        this.repositoryReply= (ReplyMessagePaginatedDatabaseRepository) repoFactory.createRepositor(DataBaseRepositoryStrategy.replymessage,null);
//        UserFilter filter=new UserFilter();
//        repositoryUser.findAll(new PageableImplementation(1, 10), filter).getContent().forEach(System.out::println);
        this.serviceUsers=new UserPagService(repositoryUser,repositoryFriendship,"DataBase");
        this.serviceFriends=new FriendshipsPagService(repositoryUser,repositoryFriendship,repositoryRequests);
        this.serviceMessages=new MessagePagService(repositoryUser,repositoryFriendship,repositoryMessage,repositoryReply);
        this.serviceMessages.getAllMessage().forEach(System.out::println);

        this.serviceMessages.getConvo("janesmith","johndoe");
        System.out.println("--------------------------------------------------");
        this.serviceMessages.getConvoObjects("johndoe","janesmith").forEach(System.out::println);
        serviceMessages.getReplies().forEach(System.out::println);
        initView(primaryStage);
        //primaryStage.setWidth(1000);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader stageLoader=new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/map_socialnetworkvt/views/LogIn-view.fxml"));
        AnchorPane setLayout=stageLoader.load();
        primaryStage.setTitle("Log In");
        primaryStage.setScene(new Scene(setLayout, Color.POWDERBLUE));
        LogInController logInController=stageLoader.getController();
        logInController.setLogInController(serviceUsers,serviceFriends,serviceMessages,primaryStage);

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
