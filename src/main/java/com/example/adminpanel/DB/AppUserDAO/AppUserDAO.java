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
                ResultSet rs = stmt.executeQuery("SELECT * FROM user_subscribe LEFT JOIN user_table ON user_subscribe.subscribe_id = user_table.subscribe");
                while (rs.next()) {
                    AppUser user = new AppUser();
                    Long id = rs.getLong("user_id");
                    user.setTelegramUserId(id);
                    Subscribe sub = new Subscribe();
                    sub.setSubscribeType(rs.getString("subscribe_type"));
                    user.setSubscribe(sub);
                    user.setUsername(rs.getString("user_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setFirstName(rs.getString("first_name"));
                    users.add(user);
                }
                rs.close();
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
                Long sub_id;
                if (!user.getSubscribe().getSubscribeType().isEmpty()) sub_id = (long) user.getSubscribe().getSubscribeId();
                else sub_id = null;
                PreparedStatement st = con.prepareStatement("UPDATE user_table SET first_name = '" + user.getFirstName() + "', last_name = '" + user.getLastName() + "', subscribe = " + sub_id + " WHERE telegram_user_id = " + user.getTelegramUserId());
                st.executeUpdate();
                st.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Overridegit 
    public int getUserSubId(Long userID) {
        int id = 0;
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user_subscribe WHERE user_id = " + userID);
                while (rs.next()) {
                    id = rs.getInt("subscribe_id");
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}