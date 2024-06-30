package com.example.adminpanel.DB.DAO.SubscribeDAO;


import com.example.adminpanel.DB.DB;
import com.example.adminpanel.DB.IDAO.IDAO;
import com.example.adminpanel.entity.Subscribe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class SubscribeDAO implements IDAO<Subscribe> {
    private DB db;

    public SubscribeDAO() {
        this.db = new DB();
    }

    @Override
    public Collection<Subscribe> getAll() {
        Collection<Subscribe> subs = new ArrayList<>();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe");
                while (rs.next()) {
                    Subscribe sub = new Subscribe();
                    sub.setSubscribeId(rs.getLong("id"));
                    sub.setSubscribeDescr(rs.getString("descr"));
                    sub.setSubscribeType(rs.getString("type"));
                    subs.add(sub);
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subs;
    }

    @Override
    public Subscribe get(Long id) {
        Subscribe sub = new Subscribe();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe WHERE id = " + id);
                while (rs.next()) {
                    sub.setSubscribeId(rs.getLong("id"));
                    sub.setSubscribeDescr(rs.getString("descr"));
                    sub.setSubscribeType(rs.getString("type"));
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sub;
    }

    public Subscribe getSubscribeByType(String type) {
        Subscribe sub = new Subscribe();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe WHERE type = '" + type + "'");
                while (rs.next()) {
                    sub.setSubscribeId(rs.getLong("id"));
                    sub.setSubscribeDescr(rs.getString("descr"));
                    sub.setSubscribeType(rs.getString("type"));
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sub;
    }

    @Override
    public void delete(Long id) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("DELETE FROM subscribe WHERE id = " + id);
                st.executeUpdate();
                st = con.prepareStatement("DELETE FROM user_subscribe WHERE subscribe_id = " + id);
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
    public void create(Subscribe subscribe) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("INSERT INTO subscribe (type, descr) VALUES (?, ?)");
                st.setString(1, subscribe.getSubscribeType());
                st.setString(2, subscribe.getSubscribeDescr());
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
    public void update(Subscribe subscribe) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("UPDATE subscribe SET type = ?, descr = ? where id = ?");
                st.setString(1, subscribe.getSubscribeType());
                st.setString(2, subscribe.getSubscribeDescr());
                st.setLong(3, subscribe.getSubscribeId());
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
