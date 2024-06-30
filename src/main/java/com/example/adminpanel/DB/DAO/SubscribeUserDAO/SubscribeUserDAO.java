package com.example.adminpanel.DB.DAO.SubscribeUserDAO;

import com.example.adminpanel.DB.DB;
import com.example.adminpanel.DB.IDAO.IDAO;
import com.example.adminpanel.entity.SubscribeUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

public class SubscribeUserDAO implements IDAO<SubscribeUser> {
    private DB db;

    public SubscribeUserDAO() {
        this.db = new DB();
    }

    @Override
    public Collection<SubscribeUser> getAll() {
        return List.of();
    }

    @Override
    public SubscribeUser get(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    public void delete(SubscribeUser SubscribeUser) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("DELETE FROM user_subscribe WHERE user_id = " + SubscribeUser.getuserId() + " AND subscribe_id =" + SubscribeUser.getsubscribeId());
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
    public void create(SubscribeUser SubscribeUser) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("INSERT INTO user_subscribe (subscribe_id, user_id) VALUES (?, ?)");
                st.setLong(1, SubscribeUser.getsubscribeId());
                st.setLong(2, SubscribeUser.getuserId());
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
    public void update(SubscribeUser subscribe) {

    }
}
