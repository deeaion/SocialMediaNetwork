package com.example.map_toysocialnetworkgradlefx.Controller;

import com.example.map_toysocialnetworkgradlefx.Domain.User;
import com.example.map_toysocialnetworkgradlefx.Service.FriendService;
import com.example.map_toysocialnetworkgradlefx.Utils.ActionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class InputUserDataController {

    @FXML
    private TextField txtFieldFirstName;

    @FXML
    private TextField txtFieldLastName;

    @FXML
    private TextField txtFieldUsernameMod;

    @FXML
    private Label lblModificationType;

    @FXML
    private Button btnSaveAction;

    @FXML
    private Button btnCancelAction;
    private DatePicker datePickerDate;
    FriendService service;
    Stage dialogStage;
    User user;
    ActionType type;
    private void initialize() {

    }

    public void setService(FriendService service, Stage stage, User user,ActionType type)
    {
        this.service=service;
        this.dialogStage=stage;
        this.user=user;
        if(null!=user)
        {
            setFields(user);
        }
        this.type=type;
        if(type==ActionType.MODIFY)
            lblModificationType.setText("Update");
        else
            lblModificationType.setText("Add");


    }
    private void clearFields()
    {
        txtFieldFirstName.setText("");
        txtFieldLastName.setText("");
        txtFieldUsernameMod.setText("");

    }

    private void setFields(User user) {
        txtFieldFirstName.setText(user.getFirstName());
        txtFieldLastName.setText(user.getLastName());
        txtFieldUsernameMod.setText(user.getUsername());
    }
    @FXML
    public void handleCancel()
    {
        dialogStage.close();
    }


    @FXML
    public void handleSave()
    {

        String FirstName=txtFieldFirstName.getText();
        String LastName=txtFieldLastName.getText();
        String username=txtFieldUsernameMod.getText();
        User u=new User(FirstName,LastName,username);
        if(ActionType.SAVE==type)
        {
            saveUser(u);
        }
        else
        {
            updateUser(u);
        }
    }

    private void updateUser(User u) {
        try
        {
            boolean done=service.updateUser(u.getFirstName(),u.getLastName(),u.getUsername());
            if (done)
            {
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Update --- result","User was updated succesfully");

            }

        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null,Alert.AlertType.ERROR,"!!!Update --- Unsuccessful",e.getMessage());

        }
    }

    private void saveUser(User u) {
        try
        {
            boolean done=this.service.addUser(u.getFirstName(),u.getLastName(),u.getUsername());
            if(done)
            {
                dialogStage.close();;
            }
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Save --- result","User was saved succesfully");
        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null,Alert.AlertType.ERROR,"!!!Save --- Unsuccessful",e.getMessage());
        }
        dialogStage.close();
    }
    public void handleClicks(ActionEvent event)
    {
        if(event.getSource()==btnSaveAction)
        {
            handleSave();
        }
        if(event.getSource()==btnCancelAction)
        {
            handleCancel();
        }
    }

}
