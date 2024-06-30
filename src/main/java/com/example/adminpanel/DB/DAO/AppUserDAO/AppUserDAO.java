package com.example.adminpanel.DB.DAO.AppUserDAO;

import com.example.adminpanel.DB.DB;
import com.example.adminpanel.DB.IDAO.IDAO;
import com.example.adminpanel.entity.AppUser;
import com.example.adminpanel.entity.Subscribe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AppUserDAO implements IDAO<AppUser> {
    private DB db;

    public AppUserDAO() {
        this.db = new DB();
    }

    @Override
    public Collection getAll() {
        return List.of();
    }

    @Override
    public AppUser get(Long id) {
        AppUser user = null;
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user_table WHERE telegram_user_id = " + id);
                user = new AppUser();
                while (rs.next()) {
                    user.setId(rs.getLong("id"));
                    user.setTelegramUserId(rs.getLong("telegram_user_id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setUsername(rs.getString("user_name"));
                }
                rs.close();
                ArrayList<Subscribe> subs = new ArrayList<>();
                rs = stmt.executeQuery("SELECT * FROM user_subscribe LEFT JOIN subscribe on subscribe.id = user_subscribe.subscribe_id WHERE user_subscribe.user_id = " + id);
                while (rs.next()) {
                    Subscribe sub = new Subscribe();
                    sub.setSubscribeType(rs.getString("type"));
                    sub.setSubscribeId(rs.getLong("subscribe_id"));
                    subs.add(sub);
                }
                user.setSubscribe(subs.toArray(new Subscribe[0]));
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void create(AppUser user) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("INSERT INTO user_table (telegram_user_id, first_name, last_name, user_name) VALUES (?, ?, ?, ?)");
                st.setLong(1, user.getTelegramUserId());
                st.setString(2, user.getFirstName());
                st.setString(3, user.getLastName());
                st.setString(4, user.getUsername());
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
    public void delete(Long id) {

    }

    @Override
    public void update(AppUser subscribe) {

    }
}