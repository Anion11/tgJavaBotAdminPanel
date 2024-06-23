module com.example.adminpanel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.adminpanel to javafx.fxml;
    exports com.example.adminpanel;
}