package com.example.adminpanel.DB.AppUserDAO;


import com.example.adminpanel.entity.AppUser;
import com.example.adminpanel.entity.Subscribe;

import java.util.ArrayList;

public interface IAppUserDAO {
    ArrayList<AppUser> getAllSubscribers();
    ArrayList<Long> getAllSubscribersIdByType(String type);
    void deleteUser(Long id);
    void updateUser(AppUser user);
}
