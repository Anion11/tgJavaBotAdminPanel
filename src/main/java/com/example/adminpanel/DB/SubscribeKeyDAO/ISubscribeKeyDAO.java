package com.example.adminpanel.DB.SubscribeKeyDAO;


import com.example.adminpanel.entity.SubscribeKey;

import java.util.ArrayList;

public interface ISubscribeKeyDAO {
    SubscribeKey[] getAllSubscribeKey();
    void setSubscribeKey(SubscribeKey key);
    int getLastId();
}
