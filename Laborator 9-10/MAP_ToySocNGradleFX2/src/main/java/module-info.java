module com.example.map_toysocngradlefx2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;
    opens com.example.map_toysocngradlefx2.Domain to javafx.base;
    opens com.example.map_toysocngradlefx2 to javafx.fxml;
    opens com.example.map_toysocngradlefx2.Controller to javafx.fxml;
    exports com.example.map_toysocngradlefx2.Controller;
    exports com.example.map_toysocngradlefx2;
    opens com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Factory to javafx.base,javafx.fxml;
    opens com.example.map_toysocngradlefx2.Repository.DataBaseRepository.Util to javafx.base,javafx.fxml;
    opens com.example.map_toysocngradlefx2.Service to java.base, javafx.fxml;
    exports com.example.map_toysocngradlefx2.Service;
    opens com.example.map_toysocngradlefx2.Domain.Message to javafx.base;
    opens com.example.map_toysocngradlefx2.Domain.Friendships to javafx.base;
    opens com.example.map_toysocngradlefx2.Repository.DataBaseRepository.MessageRepo to javafx.base, javafx.fxml;
}