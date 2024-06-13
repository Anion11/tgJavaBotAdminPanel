package com.example.adminpanel.Controllers;

import com.example.adminpanel.Application;
import com.example.adminpanel.DB.SubscribeDAO.SubscribeDAO;
import com.example.adminpanel.entity.Subscribe;
import com.example.adminpanel.generateKeys.GenerateKeys;
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
    private GenerateKeys GenerateKeys = new GenerateKeys();
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

    public void saveToTxtButtonClick(MouseEvent mouseEvent) {
        GenerateKeys.createTxt(key_table);
    }

    public void generateKeysButtonClick(MouseEvent mouseEvent) {
        if (!keys_type.getText().isEmpty() && !keys_count.getText().isEmpty()) {
            int count = Integer.parseInt(keys_count.getText().trim());
            GenerateKeys.saveKeys(keys_type.getText(), count);
            updateTableKeys();
        }
    }
}
