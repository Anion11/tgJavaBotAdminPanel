module com.example.adminpanel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.adminpanel to javafx.fxml;
    exports com.example.adminpanel;
    exports com.example.adminpanel.Controllers;
    opens com.example.adminpanel.Controllers to javafx.fxml;
}