package com.example.adminpanel.DB.DAO.SubscribeDAO;

import com.example.adminpanel.DB.DB;
import com.example.adminpanel.DB.IDAO.IDAO;
import com.example.adminpanel.entity.SubscribeKey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class KeysDAO implements IDAO<SubscribeKey> {

    private DB db;

    public KeysDAO() {
        this.db = new DB();
    }

    @Override
    public Collection<SubscribeKey> getAll() {
        Collection<SubscribeKey> subKeys = new ArrayList<>();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe_keys LEFT JOIN subscribe on subscribe.id = subscribe_keys.subscribe_id");
                while (rs.next()) {
                    SubscribeKey subKey = new SubscribeKey();
                    subKey.setSubscribeId(rs.getInt("subscribe_id"));
                    subKey.setKey(rs.getString("key"));
                    subKey.setType(rs.getString("type"));
                    subKeys.add(subKey);
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subKeys;
    }

    @Override
    public SubscribeKey get(int id) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    public void create(SubscribeKey subKey) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("INSERT INTO subscribe_keys (key, subscribe_id) VALUES (?, ?) ");
                st.setString(1, subKey.getKey());
                st.setInt(2, subKey.getSubscribeId());
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
    public void update(SubscribeKey subKey) {

    }
}
