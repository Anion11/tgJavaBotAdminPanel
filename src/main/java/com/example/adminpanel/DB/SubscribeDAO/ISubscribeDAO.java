package com.example.adminpanel.DB.SubscribeDAO;


import com.example.adminpanel.entity.AppUser;
import com.example.adminpanel.entity.Subscribe;

import java.util.ArrayList;

public interface ISubscribeDAO {
    Subscribe[] getAllSubscribeKey();
    ArrayList<String> getAllSubscribeName();
    void setSubscribeKey(Subscribe subKey);
}
