package com.example.b.service.impl;

import com.example.b.mapper.UserMapper;
import com.example.b.pojo.User;
import com.example.b.service.LoginService;
import com.example.b.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public Map<String, String> login(User user) {
        // 查询用户名是否存在
        Map<String, String> res = new HashMap<>();
        User tmp = userMapper.findByName(user.getName());
        if (tmp == null) {
            res.put("msg", "登录失败，用户名不存在");
            return res;
        }
        String token = TokenUtil.getToken(user.getName());
        res.put("msg", "登录成功");
        res.put("account", user.getName());
        res.put("token", token);
        return res;
    }
}
