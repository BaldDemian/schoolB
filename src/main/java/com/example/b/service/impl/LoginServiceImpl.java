package com.example.b.service.impl;

import com.example.b.mapper.AccMapper;
import com.example.b.pojo.Acc;
import com.example.b.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AccMapper accMapper;
    @Override
    public Map<String, String> login(Acc acc) {
        // 查询用户名是否存在
        Map<String, String> res = new HashMap<>();
        Acc tmp = accMapper.selectById(acc.getName());
        System.out.println(tmp);
        if (tmp == null) {
            res.put("msg", "登录失败，用户名不存在");
            return res;
        }
        //String token = TokenUtil.getToken(acc.getName());
        res.put("msg", "登录成功");
        res.put("account", acc.getName());
        //res.put("token", token);
        return res;
    }
}
