module com.example.adminpanel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires telegrambots.meta;
    requires telegrambots;

    opens com.example.adminpanel to javafx.fxml;
    exports com.example.adminpanel;
}