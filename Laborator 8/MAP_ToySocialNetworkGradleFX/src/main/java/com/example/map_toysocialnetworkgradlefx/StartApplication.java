package com.example.map_toysocialnetworkgradlefx;

import com.example.map_toysocialnetworkgradlefx.Controller.SocialNetworkMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.example.map_toysocialnetworkgradlefx.Domain.User;
import com.example.map_toysocialnetworkgradlefx.Domain.Validators.Validator;
import com.example.map_toysocialnetworkgradlefx.Domain.Validators.ValidatorFactory;
import com.example.map_toysocialnetworkgradlefx.Domain.Validators.ValidatorStrategy;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Factory.DataBaseRepositoryStrategy;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Factory.DatabaseRepoFactory;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Util.DataBaseAccess;
import com.example.map_toysocialnetworkgradlefx.Service.FriendService;

import java.io.IOException;
import java.sql.SQLException;

public class StartApplication extends Application {
    private DataBaseAccess data;
    private AbstractDataBaseRepository<Long, User> repositoryUser;
    private AbstractDataBaseRepository<Long, User> repositoryFriendship;
    public FriendService service;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //here i have my main
        //i am gonna declare everything i need for
        //my db app to work
        FriendService service;
        ValidatorFactory factory=new ValidatorFactory();
        Validator userValidator=factory.createValidator(ValidatorStrategy.User);
        Validator FriendValidator=factory.createValidator(ValidatorStrategy.Friendship);
        String url="jdbc:postgresql://localhost:5432/socialnetwork";
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
        initView(primaryStage);
        primaryStage.setWidth(1000);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader stageLoader=new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/map_toysocialnetworkgradlefx/views/socialnetworkmain-view.fxml"));
        AnchorPane setLayout=stageLoader.load();
        primaryStage.setTitle("Social Network");
        primaryStage.setScene(new Scene(setLayout, Color.POWDERBLUE));

        SocialNetworkMainController socialNetworkController=stageLoader.getController();
        socialNetworkController.setSocialNetworkService(service);

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
