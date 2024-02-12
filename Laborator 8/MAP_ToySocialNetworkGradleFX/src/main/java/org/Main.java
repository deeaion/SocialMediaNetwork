package org;

import com.example.map_toysocialnetworkgradlefx.Domain.Validators.Validator;
import com.example.map_toysocialnetworkgradlefx.Domain.Validators.ValidatorFactory;
import com.example.map_toysocialnetworkgradlefx.Domain.Validators.ValidatorStrategy;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Factory.DataBaseRepositoryStrategy;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Factory.DatabaseRepoFactory;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Util.AbstractDataBaseRepository;
import com.example.map_toysocialnetworkgradlefx.Repository.DataBaseRepository.Util.DataBaseAccess;
import com.example.map_toysocialnetworkgradlefx.Repository.InMemoryRepository.InMemoryRepository;
import com.example.map_toysocialnetworkgradlefx.Repository.Repository;
import com.example.map_toysocialnetworkgradlefx.Service.FriendService;
import com.example.map_toysocialnetworkgradlefx.OldUi.UI;

import java.sql.SQLException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
            FriendService service;
            String type=args[0];
            ValidatorFactory factory=new ValidatorFactory();
            Validator userValidator=factory.createValidator(ValidatorStrategy.User);
            Validator FriendValidator=factory.createValidator(ValidatorStrategy.Friendship);
            if(type.equals("Database"))
            {
                String url="jdbc:postgresql://localhost:5432/socialnetwork";
                String username="postgres";
                String password="hello";
                DataBaseAccess data=new DataBaseAccess(url,password,username);

                try {
                    data.createConnection();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                DatabaseRepoFactory repoFactory=new DatabaseRepoFactory(data);
                AbstractDataBaseRepository repositoryUser=repoFactory.createRepositor(DataBaseRepositoryStrategy.users,userValidator);
                AbstractDataBaseRepository friendsrepository=repoFactory.createRepositor(DataBaseRepositoryStrategy.friendship,FriendValidator);
                service=new FriendService(repositoryUser,friendsrepository,"database") ;
            }
            else if(type.equals("InMemory"))
            {
                Repository repositoryUser=new InMemoryRepository<>(userValidator);
                Repository friendsrepository=new InMemoryRepository(FriendValidator);
                 service=new FriendService(repositoryUser,friendsrepository,"InMemory");
            }
            else
            {
                throw new RuntimeException("Type of repo not available");
            }
            UI ui=new UI(service);
            ui.launchUI();;





        //Repository friendsrepository=new InMemoryRepository(FriendValidator);


    }
}