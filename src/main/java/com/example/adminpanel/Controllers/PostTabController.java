package com.example.adminpanel.Controllers;

import com.example.adminpanel.DB.AppUserDAO.AppUserDAO;
import com.example.adminpanel.DB.SubscribeDAO.SubscribeDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PostTabController {
    public TextField post_sub_src;
    public TextArea post_sub_descr;
    private SubscribeDAO SubscribeDAO = new SubscribeDAO();
    private String botToken = "7189595273:AAEL_fOhdd9xY31YnFwYakR0mTNAfT6PQ5E";
    public Button post_add_button;
    public ListView post_sub_type;
    private AppUserDAO AppUserDAO = new AppUserDAO();
    @FXML
    void initialize() {

        post_sub_type.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                updateTablePost();
            });
        }, 0, 100, TimeUnit.SECONDS);
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
                    sendPostWithImage(chatId);
                } else {
                    sendPostWithoutImage(chatId);
                }
            }
        }
    }
    public void sendPostWithImage(String chatId) {
        String boundary = UUID.randomUUID().toString();
        try {
            URL resourcesUrl = PostTabController.class.getResource("images/" + post_sub_src.getText());
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
    }
    public void sendPostWithoutImage(String chatId) {
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
