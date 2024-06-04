package com.example.adminpanel;

import com.example.adminpanel.DB.AppUserDAO.AppUserDAO;
import com.example.adminpanel.DB.SubscribeKeyDAO.SubscribeKeyDAO;
import com.example.adminpanel.DB.SubscribePostDAO.SubscribePostDAO;
import com.example.adminpanel.entity.AppUser;
import com.example.adminpanel.entity.Subscribe;
import com.example.adminpanel.entity.SubscribeKey;
import com.example.adminpanel.entity.SubscribePost;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    public Button keys_txt_button;
    public TableView post_table;
    public Button post_add_button;
    public TextField post_sub_type;
    public TextField post_sub_src;
    public TextArea post_sub_descr;
    public TableView user_table;
    public TextField user_type;
    public Button user_save;
    public TextField user_name;
    public TextField user_surname;
    public Button user_delete;
    private SubscribeKeyDAO SubscribeKeyDAO = new SubscribeKeyDAO();
    private SubscribePostDAO SubscribePostDAO = new SubscribePostDAO();
    private AppUserDAO AppUserDAO = new AppUserDAO();
    public TableView key_table;
    public Button keys_save_button;
    public TextField keys_count;
    public TextField keys_type;
    AppUser selectedUser = null;
    TableColumn<SubscribeKey, String> keyColumn = new TableColumn<>("Key");
    TableColumn<SubscribeKey, String> typeColumn = new TableColumn<>("Type");
    TableColumn<SubscribePost, String> typeColumnPost = new TableColumn<>("Type");
    TableColumn<SubscribePost, String> descrColumn = new TableColumn<>("Description");
    TableColumn<SubscribePost, String> srcColumn = new TableColumn<>("Image");
    TableColumn<AppUser, String> firstNameColumn = new TableColumn<>("firstName");
    TableColumn<AppUser, String> lastNameColumn = new TableColumn<>("lastName");
    TableColumn<AppUser, String> userNameColumn = new TableColumn<>("user_name");
    TableColumn<AppUser, String> subscribeColumn = new TableColumn<>("subscribe");
    @FXML
    void initialize() {
        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubscribe_type()));
        key_table.getColumns().addAll(keyColumn, typeColumn);

        descrColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescr()));
        srcColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSrc()));
        typeColumnPost.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        post_table.getColumns().addAll(typeColumnPost, srcColumn, descrColumn);

        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        subscribeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubscribe().getSubscribeType()));
        user_table.getColumns().addAll(firstNameColumn, lastNameColumn, subscribeColumn, userNameColumn);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                updateTableKeys();
                updateTableUsers();
                addEventListnerUserTable();
                updateTablePosts();
            });
        }, 0, 10, TimeUnit.SECONDS);
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

    public void updateTableKeys() {
        ObservableList<SubscribeKey> data = FXCollections.observableArrayList(SubscribeKeyDAO.getAllSubscribeKey());
        key_table.setItems(data);
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
                SubscribeKey subKey = new SubscribeKey();
                subKey.setSubscribe_type(keys_type.getText());
                subKey.setKey(sb.toString());
                SubscribeKeyDAO.setSubscribeKey(subKey);
            }
            updateTableKeys();
        }
    }
    public void updateTablePosts() {
        ObservableList<SubscribePost> data = FXCollections.observableArrayList(SubscribePostDAO.getAllPosts());
        post_table.setItems(data);
    }
    public void addPost(MouseEvent mouseEvent) {
        if (!post_sub_type.getText().isEmpty() && !post_sub_src.getText().isEmpty() && !post_sub_descr.getText().isEmpty()) {
            SubscribePost post = new SubscribePost();
            post.setType(post_sub_type.getText());
            post.setDescr(post_sub_descr.getText());
            post.setSrc(post_sub_src.getText());
            SubscribePostDAO.setPost(post);
            updateTablePosts();
        }
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
            Subscribe sub = new Subscribe();
            sub.setSubscribeType(user_type.getText());
            if (!Objects.equals(user_type.getText(), selectedUser.getSubscribe().getSubscribeType())) {
                sub.setSubscribeId(SubscribeKeyDAO.getLastId());
            } else {
                sub.setSubscribeId(AppUserDAO.getUserSubId(selectedUser.getTelegramUserId()));
            }
            user.setSubscribe(sub);
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

                user_type.setText(selectedUser.getSubscribe().getSubscribeType());
                user_name.setText(selectedUser.getFirstName());
                user_surname.setText(selectedUser.getLastName());
            }
        });
    }
}