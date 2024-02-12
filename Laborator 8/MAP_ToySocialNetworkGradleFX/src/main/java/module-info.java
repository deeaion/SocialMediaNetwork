module com.example.map_toysocialnetworkgradlefx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    opens com.example.map_toysocialnetworkgradlefx.Domain to javafx.base;
    opens com.example.map_toysocialnetworkgradlefx to javafx.fxml;
    opens com.example.map_toysocialnetworkgradlefx.Controller to javafx.fxml;
   exports com.example.map_toysocialnetworkgradlefx.Controller;
    exports com.example.map_toysocialnetworkgradlefx;
    opens com.example.map_toysocialnetworkgradlefx.Domain.Message to javafx.base;
    opens com.example.map_toysocialnetworkgradlefx.Domain.Friendships to javafx.base;
}