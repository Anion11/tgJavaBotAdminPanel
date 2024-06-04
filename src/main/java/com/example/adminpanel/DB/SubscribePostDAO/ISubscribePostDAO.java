package com.example.adminpanel.DB.SubscribePostDAO;


import com.example.adminpanel.entity.SubscribePost;

public interface ISubscribePostDAO {
    SubscribePost[] getAllPosts();
    void setPost(SubscribePost post);
}
