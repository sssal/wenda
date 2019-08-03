package com.niuke.forum.service;

import com.niuke.forum.dao.UserDAO;
import com.niuke.forum.model.User;
import com.niuke.forum.util.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public Map<String, String> register(String userName, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(userName)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(userName);
        if (user != null) {
            map.put("msg", "用户名重复");
            return map;
        }

        user = new User();
        user.setName(userName);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        user.setPassword(ForumUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        return map;
    }
}
