package com.example.adminpanel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Application.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("admin-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 941, 703);
        primaryStage.setTitle("Панель администратора!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}