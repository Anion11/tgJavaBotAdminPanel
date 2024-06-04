package com.example.adminpanel.DB.SubscribeKeyDAO;


import com.example.adminpanel.DB.DB;
import com.example.adminpanel.entity.SubscribeKey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SubscribeKeyDAO implements ISubscribeKeyDAO {
    private DB db;

    public SubscribeKeyDAO() {
        this.db = new DB();
    }

    @Override
    public SubscribeKey[] getAllSubscribeKey() {
        ArrayList<SubscribeKey> keys = new ArrayList<SubscribeKey>();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe_key");
                while (rs.next()) {
                    SubscribeKey SubKey = new SubscribeKey();
                    String key = rs.getString("key");
                    String type = rs.getString("type");
                    if (key != null) {
                        SubKey.setKey(key);
                        SubKey.setSubscribe_type(type);
                        keys.add(SubKey);
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
        return keys.toArray(new SubscribeKey[0]);
    }

    @Override
    public void setSubscribeKey(SubscribeKey key) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("INSERT INTO subscribe_key (key, type) VALUES (?, ?)");
                st.setString(1, key.getKey());
                st.setString(2, key.getSubscribe_type());
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
    public int getLastId() {
        int id = 0;
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT nextval('subscribe_key_id_seq')");

                while (rs.next()) {
                    SubscribeKey SubKey = new SubscribeKey();
                    id = rs.getInt("nextval");
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
