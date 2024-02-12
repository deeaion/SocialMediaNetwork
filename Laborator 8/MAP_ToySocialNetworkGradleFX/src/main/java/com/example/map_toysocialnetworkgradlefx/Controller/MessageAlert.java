package com.example.map_toysocialnetworkgradlefx.Controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert {
    public static void showMessage(Stage owner, Alert.AlertType type, String header, String text)
    {
        Alert message=new Alert(type);
        message.initOwner(owner);
        message.setTitle(header);
        message.setContentText(text);
        message.showAndWait();
    }
}
