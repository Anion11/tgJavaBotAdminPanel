package com.example.adminpanel.DB.SubscribeDAO;


import com.example.adminpanel.DB.DB;
import com.example.adminpanel.entity.Subscribe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SubscribeDAO implements ISubscribeDAO {
    private DB db;

    public SubscribeDAO() {
        this.db = new DB();
    }

    @Override
    public Subscribe[] getAllSubscribeKey() {
        ArrayList<Subscribe> keys = new ArrayList<Subscribe>();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe");
                while (rs.next()) {
                    Subscribe sub = new Subscribe();
                    String key = rs.getString("key");
                    String type = rs.getString("type");
                    if (key != null) {
                        sub.setSubscribeKey(key);
                        sub.setSubscribeType(type);
                        keys.add(sub);
                    };
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys.toArray(new Subscribe[0]);
    }

    @Override
    public ArrayList<String> getAllSubscribeName() {
        ArrayList<String> subscribeTypes = new ArrayList<>();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT DISTINCT type FROM subscribe");
                while (rs.next()) {
                    String type = rs.getString("type");
                    subscribeTypes.add(type);
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subscribeTypes;
    }

    @Override
    public void setSubscribeKey(Subscribe subscribe) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("INSERT INTO subscribe (key, type) VALUES (?, ?)");
                st.setString(1, subscribe.getSubscribeKey());
                st.setString(2, subscribe.getSubscribeType());
                st.executeUpdate();
                st.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
