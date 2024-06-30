package com.example.adminpanel.bot;

import com.example.adminpanel.DB.DAO.AppUserDAO.AppUserDAO;
import com.example.adminpanel.DB.DAO.SubscribeDAO.KeysDAO;
import com.example.adminpanel.DB.DAO.SubscribeDAO.SubscribeDAO;
import com.example.adminpanel.DB.DAO.SubscribeUserDAO.SubscribeUserDAO;
import com.example.adminpanel.entity.AppUser;
import com.example.adminpanel.entity.Subscribe;
import com.example.adminpanel.entity.SubscribeKey;
import com.example.adminpanel.entity.SubscribeUser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BotMediator extends TelegramLongPollingBot {
    Config config = new Config();

    AppUserDAO AppUserDAO = new AppUserDAO();
    SubscribeDAO SubscribeDAO = new SubscribeDAO();
    SubscribeUserDAO SubscribeUserDAO = new SubscribeUserDAO();
    KeysDAO KeysDAO = new KeysDAO();

    Storage storage;

    public BotMediator() {
        storage = new Storage();
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if(update.hasMessage() && update.getMessage().hasText())
            {
                Message inMess = update.getMessage();
                String chatId = inMess.getChatId().toString();
                String[] responses = parseMessage(inMess);

                SendMessage outMess = new SendMessage();
                outMess.setChatId(chatId);
                outMess.setReplyMarkup(setMenu());

                if (Objects.equals(storage.getUsersLastAnswers().get(inMess.getChatId()), "/unsubscribe")) {
                    outMess.setReplyMarkup(setButtons());
                    storage.putUsersLastAnswers(inMess.getChatId(), inMess.getText());
                }

                for (String response: responses) {
                    outMess.setText(response);
                    execute(outMess);
                }
            } else if (update.hasCallbackQuery()) {
                String callbackData = update.getCallbackQuery().getData();
                int messageId = update.getCallbackQuery().getMessage().getMessageId();
                String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
                String response;

                if (callbackData.equals("YES_BUTTON")) {
                    unSubscribeUser(update.getCallbackQuery().getFrom().getId(), storage.getUsersLastAnswers().get(update.getCallbackQuery().getFrom().getId()));
                    response = "Подписка удалена(";
                } else if (callbackData.equals("NO_BUTTON")) response =  "Отлично! Давайте продолжим!";
                else response = "Ошибка";

                EditMessageText message = new EditMessageText();
                message.setChatId(chatId);
                message.setMessageId(messageId);
                message.setText(response);

                execute(message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    private boolean checkValidKey(String key) {
        String[] keys = storage.getKeys();
        for (String k : keys) {
            if (k.equals(key)) return true;
        }
        return false;
    }

    public ReplyKeyboardMarkup setMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        Set<String> commands = storage.getCommands().keySet();
        for(String key: commands) {
            if (!Objects.equals(key, "/start")) row.add(new KeyboardButton(key));
            if (row.size() >= 3) {
                list.add(row);
                row = new KeyboardRow();
            }
        }
        if (!row.isEmpty()) list.add(row);
        keyboardMarkup.setKeyboard(list);
        return keyboardMarkup;
    }
    public InlineKeyboardMarkup setButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("ДА");
        button1.setCallbackData("YES_BUTTON");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("НЕТ");
        button2.setCallbackData("NO_BUTTON");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button1);
        row.add(button2);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }
    private String UserInfo(Long UserId) {
        Subscribe[] subs = AppUserDAO.get(UserId).getSubscribe();
        if (subs.length != 0) {
            StringBuilder sb = new StringBuilder();
            for (Subscribe subscribe : subs) {
                sb.append("Подписка активна: ").append(subscribe.getSubscribeType().toUpperCase()).append("\n");
            }
            return sb.toString();
        } else return "Подписка на данный момент не активна";
    }
    private void registerUser(Message message) {
        if (AppUserDAO.get(message.getFrom().getId()).getTelegramUserId() != null) {
            System.out.println("[log] Пользователь "+ AppUserDAO.get(message.getFrom().getId()) + " существует");
            return;
        }
        AppUser user = new AppUser();
        user.setTelegramUserId(message.getFrom().getId());
        user.setLastName(message.getFrom().getLastName());
        user.setUsername(message.getFrom().getUserName());
        user.setFirstName(message.getFrom().getFirstName());
        AppUserDAO.create(user);
    }

    private void subscribeUser(Long userId, String key) {
        SubscribeKey subscribeKey = KeysDAO.getByKey(key);
        SubscribeUser subUser = new SubscribeUser();
        subUser.setuserId(userId);
        subUser.setsubscribeId(subscribeKey.getSubscribeId());
        KeysDAO.delete(subscribeKey.getId());
        SubscribeUserDAO.create(subUser);
    }

    private void unSubscribeUser(Long userId, String type) {
        AppUser user = AppUserDAO.get(userId);
        SubscribeUser sbbUser = new SubscribeUser();
        for (Subscribe subscribe: user.getSubscribe()) {
            if (Objects.equals(subscribe.getSubscribeType(), type)) {
                sbbUser.setsubscribeId(subscribe.getSubscribeId());
                sbbUser.setuserId(userId);
            }
        }
        SubscribeUserDAO.delete(sbbUser);
    }

    private String[] parseMessage(Message message) throws TelegramApiException {
        String[] response;
        String msg = message.getText();
        if (storage.getCommands().containsKey(msg)) {
            response = storage.getCommands().get(msg);
            switch (msg) {
                case "/start":
                    registerUser(message);
                    break;
                case "/info":
                    response = new String[]{String.join(", ", storage.getCommands().get(message.getText())), UserInfo(message.getFrom().getId())};
                    break;
                case "/unsubscribe":
                    if (AppUserDAO.get(message.getFrom().getId()).getSubscribe().length == 0)  {
                        response = new String[]{"У вас еще нет подписки!"};
                        return response;
                    } else {
                        response = new String[]{"Введите тип подписки, которую хотите удалить"};;
                        storage.putUsersLastAnswers(message.getChatId(), "/unsubscribeType");
                        return response;
                    }
            }
            storage.putUsersLastAnswers(message.getChatId(), msg);
        } else if (Objects.equals(storage.getUsersLastAnswers().get(message.getChatId()), "/register")) {
            if (checkValidKey(msg)) {
                subscribeUser(message.getFrom().getId(), msg);
                response =  new String[]{"Поздравляю! Вы получили подписку!"};
            } else response =  new String[]{"Данный ключ не действителен или не существует!"};
        } else if (Objects.equals(storage.getUsersLastAnswers().get(message.getChatId()), "/unsubscribeType")) {
            Subscribe[] subs = AppUserDAO.get(message.getFrom().getId()).getSubscribe();
            Subscribe currentSub =  SubscribeDAO.getSubscribeByType(msg);
            boolean containsTarget = false;
            for (Subscribe sub : subs) {
                if (sub.getSubscribeId().equals(currentSub.getSubscribeId())) {
                    containsTarget = true;
                    break;
                }
            }
            if (containsTarget) {
                storage.putUsersLastAnswers(message.getChatId(), "/unsubscribe");
                response = storage.getCommands().get("/unsubscribe");
            } else response = new String[]{"Такой подписки у вас не найдено"};
        }
        else {
            response = new String[]{"Сообщение не распознано. Повторите попытку"};
            storage.putUsersLastAnswers(message.getChatId(), "");
        };
        return response;
    }
}