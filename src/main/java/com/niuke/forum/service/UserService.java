package com.niuke.forum.service;

import com.niuke.forum.dao.UserDAO;
import com.niuke.forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
