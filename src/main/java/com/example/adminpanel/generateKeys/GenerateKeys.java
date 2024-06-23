package com.example.adminpanel.generateKeys;

import com.example.adminpanel.Application;
import com.example.adminpanel.DB.DAO.SubscribeDAO.KeysDAO;
import com.example.adminpanel.entity.SubscribeKey;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateKeys {
    private KeysDAO KeysDAO = new KeysDAO();

    public void createTxt(TableView table) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));

        File file = fileChooser.showSaveDialog(Application.getPrimaryStage());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Ключи:\n");
            for (Object item : table.getItems()) {
                writer.write(item.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeys(int id, int count) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < count; i++) {
            StringBuilder sb = new StringBuilder(6);
            Random random = new Random();
            for (int j = 0; j < 6; j++) {
                int randomIndex = random.nextInt(characters.length());
                sb.append(characters.charAt(randomIndex));
            }
            SubscribeKey subKey = new SubscribeKey();
            subKey.setSubscribeId(id);
            subKey.setKey(sb.toString());
            KeysDAO.create(subKey);
        }
    }
}
