package com.example.adminpanel.Controllers;

import com.example.adminpanel.DB.AppUserDAO.AppUserDAO;
import com.example.adminpanel.entity.AppUser;
import com.example.adminpanel.entity.Subscribe;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UserTabController {
    TableColumn<AppUser, String> firstNameColumn = new TableColumn<>("firstName");
    TableColumn<AppUser, String> lastNameColumn = new TableColumn<>("lastName");
    TableColumn<AppUser, String> userNameColumn = new TableColumn<>("user_name");
    TableColumn<AppUser, String> subscribeColumn = new TableColumn<>("subscribe");
    AppUser selectedUser = null;
    public TextField user_surname;
    public Button user_delete;
    public TableView user_table;
    public TextField user_type;
    public Button user_save;
    public TextField user_name;
    private AppUserDAO AppUserDAO = new AppUserDAO();
    @FXML
    void initialize() {
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        subscribeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubscribeTypes()));

        user_table.getColumns().addAll(firstNameColumn, lastNameColumn, subscribeColumn, userNameColumn);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                updateTableUsers();
                addEventListnerUserTable();
            });
        }, 0, 100, TimeUnit.SECONDS);
    }
    public void updateTableUsers() {
        ObservableList<AppUser> data = FXCollections.observableArrayList(AppUserDAO.getAllSubscribers());

        user_table.setItems(data);
    }
    public void saveUser(MouseEvent mouseEvent) {
        if (user_name.getText().length() > 1) {
            AppUser user = new AppUser();
            user.setFirstName(user_name.getText());
            user.setLastName(user_surname.getText());

            ArrayList<Subscribe> subs = new ArrayList<>();
            if (!user_type.getText().isEmpty()) {
                String[] types = user_type.getText().split(" ");
                for (String type : types) {
                    Subscribe sub = new Subscribe();
                    sub.setSubscribeType(type);
                    subs.add(sub);
                }
                user.setSubscribe(subs.toArray(new Subscribe[0]));
            } else {
                user.setSubscribe(null);
            }
            user.setTelegramUserId(selectedUser.getTelegramUserId());
            AppUserDAO.updateUser(user);
            updateTableUsers();
        }
    }

    public void deleteUser(MouseEvent mouseEvent) {
        if (selectedUser != null) {
            Long ID = selectedUser.getTelegramUserId();
            AppUserDAO.deleteUser(ID);
            updateTableUsers();
        }
    }

    public void addEventListnerUserTable() {
        user_table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                selectedUser = (AppUser) user_table.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    Subscribe[] subs = selectedUser.getSubscribe();
                    String types = "";
                    for (int i = 0; i < subs.length; i++) {
                        types += subs[i].getSubscribeType();
                        if (i != subs.length - 1) {
                            types += " ";
                        }
                    }

                    user_type.setText(types);
                    user_name.setText(selectedUser.getFirstName());
                    user_surname.setText(selectedUser.getLastName());
                }
            }
        });
    }
}
