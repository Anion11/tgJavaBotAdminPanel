package com.example.adminpanel.bot;


import com.example.adminpanel.DB.DAO.SubscribeDAO.KeysDAO;
import com.example.adminpanel.entity.SubscribeKey;

import java.util.Collection;
import java.util.HashMap;

public class Storage {
    final private HashMap<String, String[]> commands = new HashMap<>();
    private final HashMap<Long, String> usersLastAnswers = new HashMap<>();
    private KeysDAO KeysDAO = new KeysDAO();
    public Storage(){
        commands.put("/start", new String[]{"Привет, я бот для проверки подписок", "Введите /register для подключения подписки"});
        commands.put("/unsubscribe", new String[]{"Вы точно уверены что хотите отписаться?"});
        commands.put("/register", new String[]{"Введите ключ для получения подписки:"});
        commands.put("/info", new String[]{"Вот информация о вашей подписке:"});
    }

    public HashMap<Long, String> getUsersLastAnswers() {
        return usersLastAnswers;
    }
    public void putUsersLastAnswers(Long id, String mes) {
        usersLastAnswers.put(id, mes);
    }
    public HashMap<String, String[]> getCommands() {
        return commands;
    }

    public String[] getKeys() {
        Collection<SubscribeKey> subKeys = KeysDAO.getAll();
        String[] keys = new String[subKeys.size()];
        int i = 0;
        for (SubscribeKey subKey: subKeys) {
            keys[i] = subKey.getKey();
            i++;
        }
        return keys;
    }
}
