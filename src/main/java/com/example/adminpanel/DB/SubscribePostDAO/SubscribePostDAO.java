package com.example.adminpanel.DB.SubscribePostDAO;


import com.example.adminpanel.DB.DB;
import com.example.adminpanel.entity.SubscribeKey;
import com.example.adminpanel.entity.SubscribePost;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SubscribePostDAO implements ISubscribePostDAO {
    private DB db;

    public SubscribePostDAO() {
        this.db = new DB();
    }

    @Override
    public SubscribePost[] getAllPosts() {
        ArrayList<SubscribePost> posts = new ArrayList<>();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe");
                while (rs.next()) {
                    SubscribePost post = new SubscribePost();
                    post.setDescr(rs.getString("descr"));
                    post.setSrc(rs.getString("src"));
                    post.setType(rs.getString("type"));
                    posts.add(post);
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts.toArray(new SubscribePost[0]);
    }
    @Override
    public void setPost(SubscribePost post) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("INSERT INTO subscribe (type, descr, src) VALUES (?, ?, ?)");
                st.setString(1, post.getType());
                st.setString(2, post.getDescr());
                st.setString(3, post.getSrc());
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
