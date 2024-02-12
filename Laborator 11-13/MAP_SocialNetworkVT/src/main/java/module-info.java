module com.example.map_socialnetworkvt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;
    opens com.example.map_socialnetworkvt.Domain to javafx.base;
    opens com.example.map_socialnetworkvt to javafx.fxml;
    opens com.example.map_socialnetworkvt.Controller to javafx.fxml;
    exports com.example.map_socialnetworkvt.Controller;
    exports com.example.map_socialnetworkvt;
    opens com.example.map_socialnetworkvt.Repository.DataBaseRepository.Factory to javafx.base,javafx.fxml;
    opens com.example.map_socialnetworkvt.Repository.DataBaseRepository.Util to javafx.base,javafx.fxml;
    opens com.example.map_socialnetworkvt.Service to java.base, javafx.fxml;
    exports com.example.map_socialnetworkvt.Service;
    opens com.example.map_socialnetworkvt.Domain.Message to javafx.base;
    opens com.example.map_socialnetworkvt.Domain.Friendships to javafx.base;
}