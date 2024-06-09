package com.example.adminpanel;

import com.example.adminpanel.DB.AppUserDAO.AppUserDAO;
import com.example.adminpanel.DB.SubscribeDAO.SubscribeDAO;
import com.example.adminpanel.entity.AppUser;
import com.example.adminpanel.entity.Subscribe;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Controller {
    private String botToken = "7189595273:AAEL_fOhdd9xY31YnFwYakR0mTNAfT6PQ5E";
    public Button keys_txt_button;
    public Button post_add_button;
    public ListView post_sub_type;
    public TextField post_sub_src;
    public TextArea post_sub_descr;
    public TableView user_table;
    public TextField user_type;
    public Button user_save;
    public TextField user_name;
    private SubscribeDAO SubscribeDAO = new SubscribeDAO();
    public TextField user_surname;
    public Button user_delete;
    private AppUserDAO AppUserDAO = new AppUserDAO();
    public TableView key_table;
    public Button keys_save_button;
    public TextField keys_count;
    public TextField keys_type;
    AppUser selectedUser = null;
    TableColumn<Subscribe, String> keyColumn = new TableColumn<>("Key");
    TableColumn<Subscribe, String> typeColumn = new TableColumn<>("Type");
    TableColumn<AppUser, String> firstNameColumn = new TableColumn<>("firstName");
    TableColumn<AppUser, String> lastNameColumn = new TableColumn<>("lastName");
    TableColumn<AppUser, String> userNameColumn = new TableColumn<>("user_name");
    TableColumn<AppUser, String> subscribeColumn = new TableColumn<>("subscribe");
    @FXML
    void initialize() {
        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubscribeKey()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubscribeType()));
        key_table.getColumns().addAll(keyColumn, typeColumn);

        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        subscribeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubscribeTypes()));
        user_table.getColumns().addAll(firstNameColumn, lastNameColumn, subscribeColumn, userNameColumn);

        post_sub_type.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                updateTableKeys();
                updateTableUsers();
                addEventListnerUserTable();
                updateTablePost();
            });
        }, 0, 100, TimeUnit.SECONDS);
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
        ObservableList<Subscribe> data = FXCollections.observableArrayList(SubscribeDAO.getAllSubscribeKey());
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
                Subscribe subKey = new Subscribe();
                subKey.setSubscribeType(keys_type.getText());
                subKey.setSubscribeKey(sb.toString());
                SubscribeDAO.setSubscribeKey(subKey);
            }
            updateTableKeys();
        }
    }
    public void updateTablePost() {
        ObservableList<String> data = FXCollections.observableArrayList(SubscribeDAO.getAllSubscribeName());
        post_sub_type.setItems(data);
    }
    public void sendPost(MouseEvent mouseEvent) {
        ObservableList selectedItems = post_sub_type.getSelectionModel().getSelectedItems();
        if (!selectedItems.isEmpty() && !post_sub_descr.getText().isEmpty()) {
            ArrayList<Long> allUsersId = new ArrayList<>();
            for(Object type : selectedItems) {
                ArrayList<Long> usersId = AppUserDAO.getAllSubscribersIdByType((String) type);
                allUsersId.addAll(usersId);
            }
            for (Long userId : new HashSet<>(allUsersId)) {
                String chatId = userId.toString();
                if (!post_sub_src.getText().isEmpty()) {
                    String boundary = UUID.randomUUID().toString();
                    try {
                        URL resourcesUrl = Controller.class.getResource("images/" + post_sub_src.getText());
                        if (resourcesUrl == null) {
                            throw new FileNotFoundException("Resource not found");
                        }

                        String resourcesPath = URLDecoder.decode(resourcesUrl.getFile(), "UTF-8");
                        File imageFile = new File(resourcesPath);
                        String attachmentName = "photo";

                        URL url = new URL("https://api.telegram.org/bot" + botToken + "/sendPhoto?chat_id=" + chatId + "&caption=" + post_sub_descr.getText());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                        try (OutputStream outputStream = conn.getOutputStream();
                             PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true)) {

                            writer.append("--" + boundary).append("\r\n");
                            writer.append("Content-Disposition: form-data; name=\"" + attachmentName + "\"; filename=\"" + imageFile.getName() + "\"").append("\r\n");
                            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(imageFile.getName())).append("\r\n");
                            writer.append("\r\n");
                            writer.flush();

                            Files.copy(imageFile.toPath(), outputStream);

                            writer.append("\r\n");
                            writer.append("--" + boundary + "--").append("\r\n");
                            writer.flush();
                        }

                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        in.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        URL url = new URL("https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chatId + "&text=" + post_sub_descr.getText());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");

                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        in.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
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