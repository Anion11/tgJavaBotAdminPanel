package com.example.adminpanel;

import com.example.adminpanel.DB.DAO.SubscribeDAO.KeysDAO;
import com.example.adminpanel.DB.DAO.SubscribeDAO.SubscribeDAO;
import com.example.adminpanel.entity.Subscribe;
import com.example.adminpanel.entity.SubscribeKey;
import com.example.adminpanel.generateKeys.GenerateKeys;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Controller {
    public TableView table;
    public TextField inputCountKeys;
    public TextField inputDescr;
    public TextField inputType;
    private GenerateKeys GenerateKeys = new GenerateKeys();
    private SubscribeDAO SubscribeDAO = new SubscribeDAO();
    private Subscribe selectedSubscribe = null;
    private KeysDAO KeysDAO = new KeysDAO();

    @FXML
    void initialize() {
        TableColumn<Subscribe, String> idColumn = new TableColumn<>("ID");
        TableColumn<Subscribe, String> typeColumn = new TableColumn<>("Тип подписки");
        TableColumn<Subscribe, String> descrColumn = new TableColumn<>("Описание");
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStringSubscribeId()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubscribeType()));
        descrColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubscribeDescr()));

        table.getColumns().addAll(idColumn, typeColumn, descrColumn);
        updateTable();

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                selectedSubscribe = (Subscribe) table.getSelectionModel().getSelectedItem();
                inputDescr.setText(selectedSubscribe.getSubscribeDescr());
                inputType.setText(selectedSubscribe.getSubscribeType());
            }
        });
    }
    private void updateTable() {
        ObservableList<Subscribe> data = FXCollections.observableArrayList(SubscribeDAO.getAll());

        table.setItems(data);
    }

    public void addSubscribe(MouseEvent mouseEvent) {
        if (!(inputDescr.getText().isEmpty() && inputType.getText().isEmpty())) {
            Subscribe sub = new Subscribe();
            sub.setSubscribeDescr(inputDescr.getText());
            sub.setSubscribeType(inputType.getText());
            SubscribeDAO.create(sub);
            updateTable();
        }
    }

    public void deleteSubscribe(MouseEvent mouseEvent) {
        if (selectedSubscribe != null) {
            SubscribeDAO.delete(selectedSubscribe.getSubscribeId());
            updateTable();
        }
    }
    public void updateSubscribe(MouseEvent mouseEvent) {
        if (selectedSubscribe != null) {
            Subscribe sub = new Subscribe();
            sub.setSubscribeId(selectedSubscribe.getSubscribeId());
            sub.setSubscribeType(inputType.getText());
            sub.setSubscribeDescr(inputDescr.getText());
            SubscribeDAO.update(sub);
            updateTable();
        }
    }

    public void createKeysSubscribe(MouseEvent mouseEvent) {
        TableView key_table = new TableView<>();
        TableColumn<String, String> keyColumn = new TableColumn<>("Key");
        TableColumn<String, String> typeColumn = new TableColumn<>("Type");

        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        key_table.getColumns().addAll(keyColumn, typeColumn);

        GenerateKeys.saveKeys(selectedSubscribe.getSubscribeId(), Integer.parseInt(inputCountKeys.getText()));
        ObservableList<SubscribeKey> data = FXCollections.observableArrayList(KeysDAO.getAll());
        key_table.setItems(data);
        GenerateKeys.createTxt(key_table);
    }
}
