package com.example.adminpanel.DB.AppUserDAO;

import com.example.adminpanel.DB.DB;
import com.example.adminpanel.entity.AppUser;
import com.example.adminpanel.entity.Subscribe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AppUserDAO implements IAppUserDAO {
    private DB db;

    public AppUserDAO() {
        this.db = new DB();
    }

    @Override
    public ArrayList<AppUser> getAllSubscribers() {
        ArrayList<AppUser> users = new ArrayList<>();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user_table");
                while (rs.next()) {
                    AppUser user = new AppUser();
                    user.setId(rs.getLong("id"));
                    user.setTelegramUserId(rs.getLong("telegram_user_id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setUsername(rs.getString("user_name"));
                    users.add(user);
                }
                rs.close();
                for (AppUser user: users) {
                    rs = stmt.executeQuery("SELECT * FROM subscribe WHERE user_id = " + user.getTelegramUserId());
                    ArrayList<Subscribe> subs = new ArrayList<>();
                    while (rs.next()) {
                        Subscribe subscribe = new Subscribe();
                        subscribe.setSubscribeType(rs.getString("type"));
                        subs.add(subscribe);
                    }
                    user.setSubscribe(subs.toArray(new Subscribe[0]));
                    rs.close();
                }
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public ArrayList<Long> getAllSubscribersIdByType(String type) {
        ArrayList<Long> usersId = new ArrayList<>();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user_table LEFT JOIN subscribe ON subscribe.user_id = user_table.telegram_user_id WHERE type = '" + type + "'");
                while (rs.next()) {
                    usersId.add(rs.getLong("telegram_user_id"));
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersId;
    }

    @Override
    public void deleteUser(Long id) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("DELETE FROM user_table WHERE telegram_user_id = ?");
                st.setLong(1, id);
                st.executeUpdate();
                st = con.prepareStatement("DELETE FROM user_subscribe WHERE user_id = ?");
                st.setLong(1, id);
                st.executeUpdate();
                st = con.prepareStatement("DELETE FROM subscribe WHERE user_id = ?");
                st.setLong(1, id);
                st.executeUpdate();
                st.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(AppUser user) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("UPDATE user_table SET first_name = '" + user.getFirstName() + "', last_name = '" + user.getLastName() + "' WHERE telegram_user_id = " + user.getTelegramUserId());
                st.executeUpdate();
                st = con.prepareStatement("DELETE FROM subscribe WHERE user_id = " + user.getTelegramUserId());
                st.executeUpdate();
                st = con.prepareStatement("DELETE FROM user_subscribe WHERE user_id = " + user.getTelegramUserId());
                st.executeUpdate();
                if (user.getSubscribe() != null) {
                    for (Subscribe sub: user.getSubscribe()) {
                        st = con.prepareStatement("INSERT INTO subscribe (type, user_id) VALUES (?, ?)");
                        st.setString(1, sub.getSubscribeType());
                        st.setLong(2, user.getTelegramUserId());
                        st.executeUpdate();
                    }
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe WHERE user_id = " + user.getTelegramUserId());
                    ArrayList<Integer> idSubs = new ArrayList<>();
                    while (rs.next()) {
                        idSubs.add(rs.getInt("id"));
                    }
                    stmt.close();
                    for (int id: idSubs) {
                        st = con.prepareStatement("INSERT INTO user_subscribe (subscribe_id, user_id) VALUES (?, ?)");
                        st.setInt(1,  id);
                        st.setLong(2, user.getTelegramUserId());
                        st.executeUpdate();
                    }
                }
                st.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}