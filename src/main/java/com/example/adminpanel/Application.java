package com.example.adminpanel;

import com.example.adminpanel.bot.BotMediator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws IOException, TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        BotMediator bot = new BotMediator();
        botsApi.registerBot(bot);

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