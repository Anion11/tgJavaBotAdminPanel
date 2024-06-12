package com.example.adminpanel.Controllers;

import com.example.adminpanel.Application;
import com.example.adminpanel.DB.SubscribeDAO.SubscribeDAO;
import com.example.adminpanel.entity.Subscribe;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GenerateKeysTabController {
    public TableView key_table;
    public Button keys_txt_button;
    public Button keys_save_button;
    public TextField keys_count;
    public TextField keys_type;
    private SubscribeDAO SubscribeDAO = new SubscribeDAO();
    TableColumn<Subscribe, String> keyColumn = new TableColumn<>("Key");
    TableColumn<Subscribe, String> typeColumn = new TableColumn<>("Type");
    @FXML
    void initialize() {
        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubscribeKey()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubscribeType()));
        key_table.getColumns().addAll(keyColumn, typeColumn);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                updateTableKeys();
            });
        }, 0, 100, TimeUnit.SECONDS);
    }
    public void updateTableKeys() {
        ObservableList<Subscribe> data = FXCollections.observableArrayList(SubscribeDAO.getAllSubscribeKey());
        key_table.setItems(data);
    }
    public void createTxt(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));

        File file = fileChooser.showSaveDialog(Application.getPrimaryStage());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Ключи:\n");
            for (Object item : key_table.getItems()) {
                writer.write(item.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveKeys(MouseEvent mouseEvent) {
        if (!keys_type.getText().isEmpty() && !keys_count.getText().isEmpty()) {
            String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
            int count = Integer.parseInt(keys_count.getText().trim());
            for (int i = 0; i < count; i++) {
                StringBuilder sb = new StringBuilder(6);
                Random random = new Random();
                for (int j = 0; j < 6; j++) {
                    int randomIndex = random.nextInt(characters.length());
                    sb.append(characters.charAt(randomIndex));
                }
                Subscribe subKey = new Subscribe();
                subKey.setSubscribeType(keys_type.getText());
                subKey.setSubscribeKey(sb.toString());
                SubscribeDAO.setSubscribeKey(subKey);
            }
            updateTableKeys();
        }
    }
}
